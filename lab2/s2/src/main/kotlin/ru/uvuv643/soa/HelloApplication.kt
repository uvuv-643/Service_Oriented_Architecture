package ru.uvuv643.soa

import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application

@ApplicationPath("/api")
class MyApplication : Application() {
}