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

public class FailedLogin {
    private static final java.sql.Connection con;
    private static final java.util.concurrent.atomic.AtomicInteger idCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    static {
        try {
            con = java.sql.DriverManager.getConnection(Database.url, Database.username, Database.password);
            try (Statement s = con.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS `FailedLogin` (id INT NOT NULL PRIMARY KEY)");
                try {
                    s.executeUpdate("ALTER TABLE `FailedLogin` ADD COLUMN username TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `FailedLogin` MODIFY COLUMN username TEXT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `FailedLogin` ADD COLUMN ipAddress TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `FailedLogin` MODIFY COLUMN ipAddress TEXT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `FailedLogin` ADD COLUMN timestamp BIGINT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `FailedLogin` MODIFY COLUMN timestamp BIGINT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `FailedLogin` ADD COLUMN reason TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `FailedLogin` MODIFY COLUMN reason TEXT NOT NULL");
                try {
                    s.executeUpdate("ALTER TABLE `FailedLogin` ADD COLUMN uuid TEXT NOT NULL");
                } catch (Exception ignored) {
                }
                s.executeUpdate("ALTER TABLE `FailedLogin` MODIFY COLUMN uuid TEXT NOT NULL");
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT id FROM `FailedLogin` ORDER BY id DESC LIMIT 1")) {
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
    public long timestamp;
    /**
     * Database field/value. Not null. <br>
     */
    public String reason;
    /**
     * Database field/value. Not null. <br>
     */
    public String uuid;

    private FailedLogin() {
    }

    /**
     * Use the static create method instead of this constructor,
     * if you plan to add this object to the database in the future, since
     * that method fetches and sets/reserves the {@link #id}.
     */
    public FailedLogin(int id, String username, String ipAddress, long timestamp, String reason, String uuid) {
        this.id = id;
        this.username = username;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.reason = reason;
        this.uuid = uuid;
    }

    /**
     * Increments the id and sets it for this object (basically reserves a space in the database).
     *
     * @return object with latest id. Should be added to the database next by you.
     */
    public static FailedLogin create(String username, String ipAddress, long timestamp, String reason, String uuid) {
        int id = idCounter.getAndIncrement();
        FailedLogin obj = new FailedLogin(id, username, ipAddress, timestamp, reason, uuid);
        return obj;
    }

    /**
     * @return a list containing all objects in this table.
     */
    public static List<FailedLogin> get() throws Exception {
        return get(null);
    }

    /**
     * @return object with the provided id.
     * @throws Exception on SQL issues, or if there is no object with the provided id in this table.
     */
    public static FailedLogin get(int id) throws Exception {
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
    public static List<FailedLogin> get(String where, Object... whereValues) throws Exception {
        List<FailedLogin> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT id,username,ipAddress,timestamp,reason,uuid" +
                        " FROM `FailedLogin`" +
                        (where != null ? ("WHERE " + where) : ""))) {
            if (where != null && whereValues != null)
                for (int i = 0; i < whereValues.length; i++) {
                    Object val = whereValues[i];
                    ps.setObject(i + 1, val);
                }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FailedLogin obj = new FailedLogin();
                list.add(obj);
                obj.id = rs.getInt(1);
                obj.username = rs.getString(2);
                obj.ipAddress = rs.getString(3);
                obj.timestamp = rs.getLong(4);
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
    public static void update(FailedLogin obj) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE `FailedLogin` SET id=?,username=?,ipAddress=?,timestamp=?,reason=?,uuid=?")) {
            ps.setInt(1, obj.id);
            ps.setString(2, obj.username);
            ps.setString(3, obj.ipAddress);
            ps.setLong(4, obj.timestamp);
            ps.setString(5, obj.reason);
            ps.setString(6, obj.uuid);
            ps.executeUpdate();
        }
    }

    /**
     * Adds the provided object to the database (note that the id is not checked for duplicates).
     */
    public static void add(FailedLogin obj) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO `FailedLogin` (id,username,ipAddress,timestamp,reason,uuid) VALUES (?,?,?,?,?,?)")) {
            ps.setInt(1, obj.id);
            ps.setString(2, obj.username);
            ps.setString(3, obj.ipAddress);
            ps.setLong(4, obj.timestamp);
            ps.setString(5, obj.reason);
            ps.setString(6, obj.uuid);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes the provided object from the database.
     */
    public static void remove(FailedLogin obj) throws Exception {
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
                "DELETE FROM `FailedLogin` WHERE " + where)) {
            if (whereValues != null)
                for (int i = 0; i < whereValues.length; i++) {
                    Object val = whereValues[i];
                    ps.setObject(i + 1, val);
                }
            ps.executeUpdate();
        }
    }

    public FailedLogin clone() {
        return new FailedLogin(this.id, this.username, this.ipAddress, this.timestamp, this.reason, this.uuid);
    }

    public String toPrintString() {
        return "" + "id=" + this.id + " " + "username=" + this.username + " " + "ipAddress=" + this.ipAddress + " " + "timestamp=" + this.timestamp + " " + "reason=" + this.reason + " " + "uuid=" + this.uuid + " ";
    }
}
