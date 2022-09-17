package com.velocitypowered.proxy.connection.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitialLoginSessionHandlerTest {

    @Test
    void GetRealMinecraftProfile() {
        var minecraftProfile = InitialLoginSessionHandler.getMinecraftProfile("Notch");
        assertNotNull(minecraftProfile);
        if (minecraftProfile.has("id")) {
            assertEquals("069a79f444e94726a5befca90e38aaf5", minecraftProfile.get("id").getAsString());
        } else {
            fail("No id field in profile");
        }
    }

    @Test
    void GetUnRealMinecraftProfile() {
        var minecraftProfile = InitialLoginSessionHandler.getMinecraftProfile("PlEaSEED0NT_CRE_"); // Random name on which minecraft premium account not exists
        if (minecraftProfile != null) {
            if (minecraftProfile.has("status")) { // MineTools API
                if (!minecraftProfile.get("status").getAsString().equals("ERR")) {
                    fail("This should have status ERR (MineTools API)");
                }
            } else { // Mojang API
                fail("This should be null (Mojang API)");
            }
        }
    }
}