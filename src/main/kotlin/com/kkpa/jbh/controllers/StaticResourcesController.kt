package com.kkpa.jbh.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream

@RestController
@RequestMapping("/docs")
class StaticResourcesController() {

    @GetMapping("/searchBy")
    @PreAuthorize("hasRole('ADMIN')")
    fun findResource(@RequestParam name: String): ResponseEntity<ByteArray> {

        println("Downloading File $name")

        val headers = HttpHeaders()
        headers.setContentType(MediaType.parseMediaType("application/pdf"))
        // Here you have to set the actual filename of your pdf
        val filename = "doc_example.pdf"
        // headers.setContentDispositionFormData(filename, filename) // Download file
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0")

        headers.set("Content-Disposition", "filename=$filename"); // display in browser.

        val iu = this.javaClass
            .getResourceAsStream("/docs/doc_example.pdf")

        val outputStream = ByteArrayOutputStream()
        iu.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        val contents = outputStream.toByteArray()

        return ResponseEntity<ByteArray>(contents, headers, HttpStatus.OK)
    }
}