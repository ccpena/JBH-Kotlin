package com.kkpa.jbh.services.users

import com.kkpa.jbh.domain.toDTOList
import com.kkpa.jbh.dto.UserDTO
import com.kkpa.jbh.exceptions.web.ResourceNotFoundException
import com.kkpa.jbh.repositories.users.UserRepository
import com.kkpa.jbh.services.CrudService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository
) : CrudService<UserDTO>(), UserService {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    companion object {
        val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    override fun validateSaveOperation(dto: UserDTO) {
        if (!isNewAndUnique(dto)) {
            throw IllegalArgumentException("User ${dto.email} - ${dto.nickName} already was registered.")
        }
    }

    override fun isNewAndUnique(dto: UserDTO): Boolean {
        return dto.id == null && findByUniqueUser(dto) == null
    }

    @Transactional(readOnly = true)
    override fun findByUniqueUser(userDTO: UserDTO): UserDTO? {
        try {
            return userRepository.findByNickNameOrEmail(
                nickName = userDTO.nickName,
                email = userDTO.email
            )?.toDTO() ?: null
        } catch (e: org.springframework.dao.IncorrectResultSizeDataAccessException) {
            throw IllegalArgumentException("Nickname and email already were registered!")
        }
    }

    override fun saveOperation(userDTO: UserDTO): UserDTO {
        log.warn("Verifying Password...")
        var userToSave = userDTO.toDomain();
        return userRepository.save(userToSave).toDTO()
    }

    override fun validateUpdateOperation(dto: UserDTO) {
        userAlreadyExistsThenThrowAnException(dto)
    }

    fun userAlreadyExistsThenThrowAnException(dto: UserDTO) {
        val userBD = findByUniqueUser(dto)
        userBD?.let {

            dto.id?.let {
                if (userBD.id == dto.id) return
            }

            val propertyExisting = if (dto.email == it.email) "${dto.email}"
            else
                "${dto.nickName}"
            throw IllegalArgumentException("User [$propertyExisting] was already Registered")
        }
    }

    override fun updateOperation(dto: UserDTO): UserDTO {
        return userRepository.save(dto.toDomain()).toDTO()
    }

    override fun delete(dto: UserDTO): Boolean {
        dto?.also {
            try {
                userRepository.deleteById(dto.id!!); return true
            } catch (enf: Exception) {
                throw java.lang.IllegalArgumentException("User  $it not found")
            }
        }

        throw java.lang.IllegalArgumentException("Not user to be deleted")
    }

    override fun findAll(): List<UserDTO> {
        val users = userRepository.findAll()

        return users.toDTOList()
    }

    override fun findById(id: UUID): UserDTO? {
        val user = userRepository.findById(id).orElse(null)

        user?.let { it.toDTO() }

        return null
    }

    override fun existsByNickName(nickName: String): Boolean {
        return userRepository.existsByNickName(nickName)
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun findByNickName(nickName: String): UserDTO {
        userRepository.findByNickName(nickName)?.let {
            return it.toDTO()
        }
        throw ResourceNotFoundException("User", "nickname", nickName)
    }
}
