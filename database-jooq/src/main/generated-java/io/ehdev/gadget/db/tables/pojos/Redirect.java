/*
 * This file is generated by jOOQ.
 */
package io.ehdev.gadget.db.tables.pojos;


import java.io.Serializable;
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
public class Redirect implements Serializable {

    private static final long serialVersionUID = -1889088292;

    private Long    redirectId;
    private String  alias;
    private String  variables;
    private String  destination;
    private String  user;
    private Long    updateCount;
    private Instant createdAt;

    public Redirect() {}

    public Redirect(Redirect value) {
        this.redirectId = value.redirectId;
        this.alias = value.alias;
        this.variables = value.variables;
        this.destination = value.destination;
        this.user = value.user;
        this.updateCount = value.updateCount;
        this.createdAt = value.createdAt;
    }

    public Redirect(
        Long    redirectId,
        String  alias,
        String  variables,
        String  destination,
        String  user,
        Long    updateCount,
        Instant createdAt
    ) {
        this.redirectId = redirectId;
        this.alias = alias;
        this.variables = variables;
        this.destination = destination;
        this.user = user;
        this.updateCount = updateCount;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redirect_id", unique = true, nullable = false, precision = 19)
    public Long getRedirectId() {
        return this.redirectId;
    }

    public void setRedirectId(Long redirectId) {
        this.redirectId = redirectId;
    }

    @Column(name = "alias", unique = true, nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "variables", length = 256)
    @Size(max = 256)
    public String getVariables() {
        return this.variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    @Column(name = "destination", nullable = false, length = 4096)
    @NotNull
    @Size(max = 4096)
    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Column(name = "user", nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Column(name = "update_count", nullable = false, precision = 19)
    @NotNull
    public Long getUpdateCount() {
        return this.updateCount;
    }

    public void setUpdateCount(Long updateCount) {
        this.updateCount = updateCount;
    }

    @Column(name = "created_at", nullable = false)
    @NotNull
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Redirect (");

        sb.append(redirectId);
        sb.append(", ").append(alias);
        sb.append(", ").append(variables);
        sb.append(", ").append(destination);
        sb.append(", ").append(user);
        sb.append(", ").append(updateCount);
        sb.append(", ").append(createdAt);

        sb.append(")");
        return sb.toString();
    }
}
