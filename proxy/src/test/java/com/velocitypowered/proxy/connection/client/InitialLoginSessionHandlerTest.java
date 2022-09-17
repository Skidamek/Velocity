package com.velocitypowered.proxy.connection.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitialLoginSessionHandlerTest {

    @Test
    void GetRealMinecraftProfile() {
        assertTrue(InitialLoginSessionHandler.isRegisteredAtMojang("Notch"));
    }

    @Test
    void GetUnRealMinecraftProfile() {
        assertFalse(InitialLoginSessionHandler.isRegisteredAtMojang("PlEaSEED0NT_CRE_18sdad9"));
    }
}