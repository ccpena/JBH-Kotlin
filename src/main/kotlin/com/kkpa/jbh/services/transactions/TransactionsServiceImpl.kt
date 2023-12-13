package com.kkpa.jbh.services.transactions

import com.kkpa.jbh.domain.accounts.Accounts
import com.kkpa.jbh.domain.toDTOList
import com.kkpa.jbh.dto.TransactionDTO
import com.kkpa.jbh.repositories.transactions.TransactionsRepository
import com.kkpa.jbh.services.CrudService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionsServiceImpl(
    private val transactionRepository: TransactionsRepository
) : TransactionsService, CrudService<TransactionDTO>() {

    override fun findAll(): List<TransactionDTO> = transactionRepository.findAll().toDTOList()

    override fun findById(id: UUID): TransactionDTO? {
        transactionRepository.findById(id).orElse(null)?.let {
            return it.toDTO()
        }
        return null
    }

    override fun isNewAndUnique(dto: TransactionDTO): Boolean = dto.id == null

    override fun validateSaveOperation(dto: TransactionDTO) {
        if (isNewAndUnique(dto).not()) throw IllegalArgumentException("Transaction is not unique")
        isNotAssociatedToAccountAndSubCategoryThrow(dto)
    }

    fun isNotAssociatedToAccountAndSubCategoryThrow(transactionDTO: TransactionDTO) {
        (transactionDTO.accountId == null || transactionDTO.subCategoryDTO == null).let {
            if (it) throw IllegalArgumentException("Transactions is not associated to an accountId.")
        }
    }

    override fun saveOperation(dto: TransactionDTO): TransactionDTO {
        return transactionRepository.save(dto.toDomain()).toDTO()
    }

    override fun validateUpdateOperation(dto: TransactionDTO) {
        if (isNewAndUnique(dto)) throw IllegalArgumentException("Transaction should be saved first")

        isNotAssociatedToAccountAndSubCategoryThrow(dto)
    }

    override fun updateOperation(dto: TransactionDTO): TransactionDTO {
        return transactionRepository.save(dto.toDomain()).toDTO()
    }

    override fun delete(dto: TransactionDTO): Boolean {
        dto?.also {
            try {
                transactionRepository.deleteById(dto.id!!); return true
            } catch (enf: Exception) {
                throw java.lang.IllegalArgumentException("Transaction $it not found")
            }
        }

        throw java.lang.IllegalArgumentException("Not user to be deleted")
    }

    fun findByAccount(accountId: UUID): List<TransactionDTO> {
        return transactionRepository.findByAccount(Accounts(id = accountId)).toDTOList()
    }
}