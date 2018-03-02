package com.xyzz.gsdd.samplespringboot2.services

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.xyzz.gsdd.samplespringboot2.models.Person
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest
class PersonRegistrationServiceTest {
    @Autowired
    private lateinit var personRegistrationService: PersonRegistrationService

    @get:Rule
    val wireMockRule = WireMockRule(8090)

    @Test
    fun `should call registrationService`() {
        val person = Person("firstName", "lastName")
        stubFor(post("/register")
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_UTF8_VALUE)
                                .withBody("""{
                            | "firstName": "${person.firstName}",
                            | "lastName": "${person.lastName}",
                            | "id": "ee0495d7-348f-44fc-968c-1d03104c0456"
                            |}
                        """.trimMargin())
                ))

        val result = personRegistrationService.addPerson(person)

        assertThat(result.firstName).isEqualTo(person.firstName)
        assertThat(result.lastName).isEqualTo(person.lastName)
        assertThat(result.id).isNotNull()

        verify(postRequestedFor(urlMatching("/register"))
                .withRequestBody(matchingJsonPath("firstName"))
                .withRequestBody(matchingJsonPath("lastName"))
                .withHeader("Content-Type", matching(APPLICATION_JSON_UTF8_VALUE)))
    }
}
