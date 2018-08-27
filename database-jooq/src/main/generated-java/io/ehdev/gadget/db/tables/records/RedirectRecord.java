/*
 * This file is generated by jOOQ.
 */
package io.ehdev.gadget.db.tables.records;


import io.ehdev.gadget.db.tables.RedirectTable;

import java.time.Instant;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "redirect", schema = "gadget", indexes = {
    @Index(name = "alias", unique = true, columnList = "alias ASC"),
    @Index(name = "PRIMARY", unique = true, columnList = "redirect_id ASC")
})
public class RedirectRecord extends UpdatableRecordImpl<RedirectRecord> implements Record7<Long, String, String, String, String, Long, Instant> {

    private static final long serialVersionUID = -1321828978;

    /**
     * Setter for <code>gadget.redirect.redirect_id</code>.
     */
    public void setRedirectId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>gadget.redirect.redirect_id</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redirect_id", unique = true, nullable = false, precision = 19)
    public Long getRedirectId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>gadget.redirect.alias</code>.
     */
    public void setAlias(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>gadget.redirect.alias</code>.
     */
    @Column(name = "alias", unique = true, nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getAlias() {
        return (String) get(1);
    }

    /**
     * Setter for <code>gadget.redirect.variables</code>.
     */
    public void setVariables(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>gadget.redirect.variables</code>.
     */
    @Column(name = "variables", length = 256)
    @Size(max = 256)
    public String getVariables() {
        return (String) get(2);
    }

    /**
     * Setter for <code>gadget.redirect.destination</code>.
     */
    public void setDestination(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>gadget.redirect.destination</code>.
     */
    @Column(name = "destination", nullable = false, length = 4096)
    @NotNull
    @Size(max = 4096)
    public String getDestination() {
        return (String) get(3);
    }

    /**
     * Setter for <code>gadget.redirect.user</code>.
     */
    public void setUser(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>gadget.redirect.user</code>.
     */
    @Column(name = "user", nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getUser() {
        return (String) get(4);
    }

    /**
     * Setter for <code>gadget.redirect.update_count</code>.
     */
    public void setUpdateCount(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>gadget.redirect.update_count</code>.
     */
    @Column(name = "update_count", nullable = false, precision = 19)
    @NotNull
    public Long getUpdateCount() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>gadget.redirect.created_at</code>.
     */
    public void setCreatedAt(Instant value) {
        set(6, value);
    }

    /**
     * Getter for <code>gadget.redirect.created_at</code>.
     */
    @Column(name = "created_at", nullable = false)
    @NotNull
    public Instant getCreatedAt() {
        return (Instant) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, String, String, String, Long, Instant> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, String, String, String, Long, Instant> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return RedirectTable.REDIRECT.REDIRECT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return RedirectTable.REDIRECT.ALIAS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return RedirectTable.REDIRECT.VARIABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return RedirectTable.REDIRECT.DESTINATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return RedirectTable.REDIRECT.USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return RedirectTable.REDIRECT.UPDATE_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Instant> field7() {
        return RedirectTable.REDIRECT.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getRedirectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getAlias();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getVariables();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getDestination();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component6() {
        return getUpdateCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant component7() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getRedirectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getAlias();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getVariables();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getDestination();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getUpdateCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant value7() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value1(Long value) {
        setRedirectId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value2(String value) {
        setAlias(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value3(String value) {
        setVariables(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value4(String value) {
        setDestination(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value5(String value) {
        setUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value6(Long value) {
        setUpdateCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord value7(Instant value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectRecord values(Long value1, String value2, String value3, String value4, String value5, Long value6, Instant value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RedirectRecord
     */
    public RedirectRecord() {
        super(RedirectTable.REDIRECT);
    }

    /**
     * Create a detached, initialised RedirectRecord
     */
    public RedirectRecord(Long redirectId, String alias, String variables, String destination, String user, Long updateCount, Instant createdAt) {
        super(RedirectTable.REDIRECT);

        set(0, redirectId);
        set(1, alias);
        set(2, variables);
        set(3, destination);
        set(4, user);
        set(5, updateCount);
        set(6, createdAt);
    }
}
