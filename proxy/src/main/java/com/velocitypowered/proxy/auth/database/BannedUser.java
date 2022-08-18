/*
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.velocitypowered.proxy.auth.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BannedUser {
    private static final java.sql.Connection con;
    private static final java.util.concurrent.atomic.AtomicInteger idCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    static {
        try {
            con = java.sql.DriverManager.getConnection(Database.url, Database.username, Database.password);
            try (Statement s = con.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS `BannedUser` (id INT NOT NULL PRIMARY KEY)");
                try {
                    s.executeUpdate("ALTER TABLE `BannedUser` ADD COLUMN username TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `BannedUser` MODIFY COLUMN username TEXT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `BannedUser` ADD COLUMN ipAddress TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `BannedUser` MODIFY COLUMN ipAddress TEXT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `BannedUser` ADD COLUMN timestampExpires BIGINT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `BannedUser` MODIFY COLUMN timestampExpires BIGINT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `BannedUser` ADD COLUMN reason TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `BannedUser` MODIFY COLUMN reason TEXT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `BannedUser` ADD COLUMN uuid TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `BannedUser` MODIFY COLUMN uuid TEXT NOT NULL");
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT id FROM `BannedUser` ORDER BY id DESC LIMIT 1")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) idCounter.set(rs.getInt(1) + 1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Database field/value. Not null. <br>
     */
    public int id;
    /**
     * Database field/value. Not null. <br>
     */
    public String username;
    /**
     * Database field/value. Not null. <br>
     */
    public String ipAddress;
    /**
     * Database field/value. Not null. <br>
     */
    public long timestampExpires;
    /**
     * Database field/value. Not null. <br>
     */
    public String reason;
    /**
     * Database field/value. Not null. <br>
     */
    public String uuid;

    private BannedUser() {
    }

    /**
     * Use the static create method instead of this constructor,
     * if you plan to add this object to the database in the future, since
     * that method fetches and sets/reserves the {@link #id}.
     */
    public BannedUser(int id, String username, String ipAddress, long timestampExpires, String reason, String uuid) {
        this.id = id;
        this.username = username;
        this.ipAddress = ipAddress;
        this.timestampExpires = timestampExpires;
        this.reason = reason;
        this.uuid = uuid;
    }

    /**
     * Increments the id and sets it for this object (basically reserves a space in the database).
     *
     * @return object with latest id. Should be added to the database next by you.
     */
    public static BannedUser create(String username, String ipAddress, long timestampExpires, String reason, String uuid) {
        int id = idCounter.getAndIncrement();
        BannedUser obj = new BannedUser(id, username, ipAddress, timestampExpires, reason, uuid);
        return obj;
    }

    /**
     * @return a list containing all objects in this table.
     */
    public static List<BannedUser> get() throws Exception {
        return get(null);
    }

    /**
     * @return object with the provided id.
     * @throws Exception on SQL issues, or if there is no object with the provided id in this table.
     */
    public static BannedUser get(int id) throws Exception {
        return get("id = " + id).get(0);
    }

    /**
     * Example: <br>
     * get("username=? AND age=?", "Peter", 33);  <br>
     *
     * @param where       can be null. Your SQL WHERE statement (without the leading WHERE).
     * @param whereValues can be null. Your SQL WHERE statement values to set for '?'.
     * @return a list containing only objects that match the provided SQL WHERE statement.
     * if that statement is null, returns all the contents of this table.
     */
    public static List<BannedUser> get(String where, Object... whereValues) throws Exception {
        List<BannedUser> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT id,username,ipAddress,timestampExpires,reason,uuid" +
                        " FROM `BannedUser`" +
                        (where != null ? ("WHERE " + where) : ""))) {
            if (where != null && whereValues != null)
                for (int i = 0; i < whereValues.length; i++) {
                    Object val = whereValues[i];
                    ps.setObject(i + 1, val);
                }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BannedUser obj = new BannedUser();
                list.add(obj);
                obj.id = rs.getInt(1);
                obj.username = rs.getString(2);
                obj.ipAddress = rs.getString(3);
                obj.timestampExpires = rs.getLong(4);
                obj.reason = rs.getString(5);
                obj.uuid = rs.getString(6);
            }
        }
        return list;
    }

    /**
     * Searches the provided object in the database (by its id),
     * and updates all its fields.
     *
     * @throws Exception when failed to find by id.
     */
    public static void update(BannedUser obj) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE `BannedUser` SET id=?,username=?,ipAddress=?,timestampExpires=?,reason=?,uuid=?")) {
            ps.setInt(1, obj.id);
            ps.setString(2, obj.username);
            ps.setString(3, obj.ipAddress);
            ps.setLong(4, obj.timestampExpires);
            ps.setString(5, obj.reason);
            ps.setString(6, obj.uuid);
            ps.executeUpdate();
        }
    }

    /**
     * Adds the provided object to the database (note that the id is not checked for duplicates).
     */
    public static void add(BannedUser obj) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO `BannedUser` (id,username,ipAddress,timestampExpires,reason,uuid) VALUES (?,?,?,?,?,?)")) {
            ps.setInt(1, obj.id);
            ps.setString(2, obj.username);
            ps.setString(3, obj.ipAddress);
            ps.setLong(4, obj.timestampExpires);
            ps.setString(5, obj.reason);
            ps.setString(6, obj.uuid);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes the provided object from the database.
     */
    public static void remove(BannedUser obj) throws Exception {
        remove("id = " + obj.id);
    }

    /**
     * Example: <br>
     * remove("username=?", "Peter"); <br>
     * Deletes the objects that are found by the provided SQL WHERE statement, from the database.
     *
     * @param whereValues can be null. Your SQL WHERE statement values to set for '?'.
     */
    public static void remove(String where, Object... whereValues) throws Exception {
        java.util.Objects.requireNonNull(where);
        try (PreparedStatement ps = con.prepareStatement(
                "DELETE FROM `BannedUser` WHERE " + where)) {
            if (whereValues != null)
                for (int i = 0; i < whereValues.length; i++) {
                    Object val = whereValues[i];
                    ps.setObject(i + 1, val);
                }
            ps.executeUpdate();
        }
    }

    public static boolean isBanned(String ipAddress, String uuid) throws Exception {
        return getBanned(ipAddress, uuid) != null;
    }

    public static BannedUser getBanned(String ipAddress, String uuid) throws Exception {
        List<BannedUser> bannedUsers = get("(ipAddress=? OR uuid=?) AND timestampExpires>?", ipAddress,
                uuid, System.currentTimeMillis());
        if (bannedUsers.isEmpty()) return null;
        if (bannedUsers.size() > 1)
            throw new RuntimeException("There cannot be multiple(" + bannedUsers.size() + ") banned users with the same ipAddress(" +
                    ipAddress + ") or UUID(" + uuid + ")! Please fix this.");
        return bannedUsers.get(0);
    }

    public static List<BannedUser> getBannedUsernames(String username) throws Exception {
        return get("username=? AND timestampExpires>?", username, System.currentTimeMillis());
    }

    public static List<BannedUser> getBannedIpAddresses(String ipAddress) throws Exception {
        return get("ipAddress=? AND timestampExpires>?", ipAddress, System.currentTimeMillis());
    }

    public BannedUser clone() {
        return new BannedUser(this.id, this.username, this.ipAddress, this.timestampExpires, this.reason, this.uuid);
    }

    public String toPrintString() {
        return "" + "id=" + this.id + " " + "username=" + this.username + " " + "ipAddress=" + this.ipAddress + " " + "timestampExpires=" + this.timestampExpires + " " + "reason=" + this.reason + " " + "uuid=" + this.uuid + " ";
    }
}

