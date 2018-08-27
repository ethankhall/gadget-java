/*
 * This file is generated by jOOQ.
 */
package io.ehdev.gadget.db.tables.records;


import io.ehdev.gadget.db.tables.RedirectHistoryTable;

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
@Table(name = "redirect_history", schema = "gadget", indexes = {
    @Index(name = "PRIMARY", unique = true, columnList = "redirect_history_id ASC")
})
public class RedirectHistoryRecord extends UpdatableRecordImpl<RedirectHistoryRecord> implements Record7<Long, String, String, String, String, Instant, Instant> {

    private static final long serialVersionUID = -452463883;

    /**
     * Setter for <code>gadget.redirect_history.redirect_history_id</code>.
     */
    public void setRedirectHistoryId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.redirect_history_id</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redirect_history_id", unique = true, nullable = false, precision = 19)
    public Long getRedirectHistoryId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>gadget.redirect_history.alias</code>.
     */
    public void setAlias(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.alias</code>.
     */
    @Column(name = "alias", nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getAlias() {
        return (String) get(1);
    }

    /**
     * Setter for <code>gadget.redirect_history.variables</code>.
     */
    public void setVariables(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.variables</code>.
     */
    @Column(name = "variables", length = 256)
    @Size(max = 256)
    public String getVariables() {
        return (String) get(2);
    }

    /**
     * Setter for <code>gadget.redirect_history.destination</code>.
     */
    public void setDestination(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.destination</code>.
     */
    @Column(name = "destination", nullable = false, length = 4096)
    @NotNull
    @Size(max = 4096)
    public String getDestination() {
        return (String) get(3);
    }

    /**
     * Setter for <code>gadget.redirect_history.user</code>.
     */
    public void setUser(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.user</code>.
     */
    @Column(name = "user", nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getUser() {
        return (String) get(4);
    }

    /**
     * Setter for <code>gadget.redirect_history.created_at</code>.
     */
    public void setCreatedAt(Instant value) {
        set(5, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.created_at</code>.
     */
    @Column(name = "created_at", nullable = false)
    @NotNull
    public Instant getCreatedAt() {
        return (Instant) get(5);
    }

    /**
     * Setter for <code>gadget.redirect_history.deleted_at</code>.
     */
    public void setDeletedAt(Instant value) {
        set(6, value);
    }

    /**
     * Getter for <code>gadget.redirect_history.deleted_at</code>.
     */
    @Column(name = "deleted_at", nullable = false)
    @NotNull
    public Instant getDeletedAt() {
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
    public Row7<Long, String, String, String, String, Instant, Instant> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, String, String, String, Instant, Instant> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return RedirectHistoryTable.REDIRECT_HISTORY.REDIRECT_HISTORY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return RedirectHistoryTable.REDIRECT_HISTORY.ALIAS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return RedirectHistoryTable.REDIRECT_HISTORY.VARIABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return RedirectHistoryTable.REDIRECT_HISTORY.DESTINATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return RedirectHistoryTable.REDIRECT_HISTORY.USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Instant> field6() {
        return RedirectHistoryTable.REDIRECT_HISTORY.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Instant> field7() {
        return RedirectHistoryTable.REDIRECT_HISTORY.DELETED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getRedirectHistoryId();
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
    public Instant component6() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant component7() {
        return getDeletedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getRedirectHistoryId();
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
    public Instant value6() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant value7() {
        return getDeletedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value1(Long value) {
        setRedirectHistoryId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value2(String value) {
        setAlias(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value3(String value) {
        setVariables(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value4(String value) {
        setDestination(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value5(String value) {
        setUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value6(Instant value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord value7(Instant value) {
        setDeletedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedirectHistoryRecord values(Long value1, String value2, String value3, String value4, String value5, Instant value6, Instant value7) {
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
     * Create a detached RedirectHistoryRecord
     */
    public RedirectHistoryRecord() {
        super(RedirectHistoryTable.REDIRECT_HISTORY);
    }

    /**
     * Create a detached, initialised RedirectHistoryRecord
     */
    public RedirectHistoryRecord(Long redirectHistoryId, String alias, String variables, String destination, String user, Instant createdAt, Instant deletedAt) {
        super(RedirectHistoryTable.REDIRECT_HISTORY);

        set(0, redirectHistoryId);
        set(1, alias);
        set(2, variables);
        set(3, destination);
        set(4, user);
        set(5, createdAt);
        set(6, deletedAt);
    }
}
