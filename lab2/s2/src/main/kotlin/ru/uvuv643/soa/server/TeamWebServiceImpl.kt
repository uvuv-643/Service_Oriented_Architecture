package ru.uvuv643.soa.server

import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.uvuv643.soa.api.v1.TeamWebService
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.PairTeamHumanDto
import java.net.URL
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


@Path("/v1/team/")
open class TeamWebServiceImpl : TeamWebService {

    override fun getHeroes(): Response? {
        DatabaseConfig.getConnection().use { connection ->
            val heroIds = mutableListOf<PairTeamHumanDto>()
            val getHeroIdsSQL = "SELECT team_id, hero_id FROM team_heroes"
            try {
                connection.prepareStatement(getHeroIdsSQL).use { stmt ->
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        heroIds.add(
                            PairTeamHumanDto(rs.getInt("hero_id"), rs.getInt("team_id"))
                        )
                    }
                }
                return Response.ok(ListHumanBeingDto(heroIds)).type(MediaType.APPLICATION_XML_TYPE).build()
            } catch (ex: Exception) {
                ex.printStackTrace()
                return Response.status(500).entity(ex.message).build()
            }
        }

    }

    override fun addHeroToTheTeam(heroId: Int, teamId: Int): Response? {
        DatabaseConfig.getConnection().use { connection ->

            val checkHeroInTeamSQL = "SELECT COUNT(*) FROM team_heroes WHERE hero_id = ? AND team_id = ?"

            val insertHeroSQL = "INSERT INTO team_heroes (hero_id, team_id) VALUES (?, ?)"

            try {

                connection.prepareStatement(checkHeroInTeamSQL).use { stmt ->
                    stmt.setInt(1, heroId)
                    stmt.setInt(2, teamId)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        val count = rs.getInt(1)
                        if (count > 0) {
                            return Response.status(400).entity("Hero is already in the team").build()
                        }
                    }
                }

                connection.prepareStatement(insertHeroSQL).use { stmt ->
                    stmt.setInt(1, heroId)
                    stmt.setInt(2, teamId)
                    stmt.executeUpdate()
                }

                return Response.ok().build()

            } catch (ex:Exception) {
                ex.printStackTrace()
                return Response.status(500).entity(ex.message).build()
            }
        }
    }

    override fun makeTeamDepressive(teamId: Int): Response? {

        System.setProperty("javax.net.debug", "ssl,handshake")

//        val keystorePath = "/certs/keystore.jks"
//        val truststorePath = "/certs/truststore.jks"
//        val password = "changeit".toCharArray()
//
//        val sslContext = SSLContext.getInstance("TLS")
//        FileInputStream(keystorePath).use { keyStoreStream ->
//            FileInputStream(truststorePath).use { trustStoreStream ->
//                val keyStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType())
//                keyStore.load(keyStoreStream, password)
//
//                val trustStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType())
//                trustStore.load(trustStoreStream, password)
//
//                val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
//                keyManagerFactory.init(keyStore, password)
//
//                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//                trustManagerFactory.init(trustStore)
//
//                sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)
//            }
//        }
//
//        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)


        HttpsURLConnection.setDefaultHostnameVerifier { hostname: String?, session: SSLSession? -> true }
        DatabaseConfig.getConnection().use { connection ->
            val heroIds = mutableListOf<Int>()

            val getHeroIdsSQL = "SELECT hero_id FROM team_heroes WHERE team_id = ?"

            try {

                connection.prepareStatement(getHeroIdsSQL).use { stmt ->
                    stmt.setInt(1, teamId)
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        heroIds.add(rs.getInt("hero_id"))
                    }
                }

                if (heroIds.isEmpty()) {
                    return Response.status(404).entity("No heroes found in the team").build()
                }

                // Prepare the XML payload
                val xmlPayload = """
                <?xml version="1.0" encoding="UTF-8"?>
                    <modifyHumanBeingRequest>
                        <mood>SORROW</mood>
                    </modifyHumanBeingRequest>
                """.trimIndent()


                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }

                    override fun checkClientTrusted(
                        certs: Array<X509Certificate>, authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        certs: Array<X509Certificate>, authType: String
                    ) {
                    }
                }
                )


                try {
                    val sc = SSLContext.getInstance("SSL")
                    sc.init(null, trustAllCerts, SecureRandom())
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
                } catch (e: GeneralSecurityException) {
                }


                for (heroId in heroIds) {
                    val url = URL("https://payara:8181/s1-1.0-SNAPSHOT/api/v1/human-being/$heroId")
                    val httpsConnection = url.openConnection() as HttpsURLConnection

                    httpsConnection.requestMethod = "PUT"
                    httpsConnection.setRequestProperty("Content-Type", "application/xml")
                    httpsConnection.setRequestProperty("Accept", "application/xml")
                    httpsConnection.doOutput = true

                    httpsConnection.outputStream.use { outputStream ->
                        val input = xmlPayload.toByteArray(Charsets.UTF_8)
                        outputStream.write(input, 0, input.size)
                        outputStream.flush()
                    }

                    val responseCode = httpsConnection.responseCode
                    val responseMessage = httpsConnection.responseMessage
                    println("Response Code for hero $heroId: $responseCode")
                    println("Response Message: $responseMessage")

                    val responseBody = if (responseCode in 200..299) {
                        httpsConnection.inputStream.bufferedReader().use { it.readText() }
                    } else {
                        httpsConnection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                    }
                    println("Response Body: $responseBody")
                }

                return Response.ok().entity("Team made depressive").build()

            } catch (ex: Exception) {
                ex.printStackTrace()
                return Response.status(500).entity(ex.message).build()
            }
        }
    }


}








































