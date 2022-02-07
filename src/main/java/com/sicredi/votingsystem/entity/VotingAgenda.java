package com.sicredi.votingsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "en_voting_agenda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotingAgenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voting_agenda_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "closed_at", nullable = false)
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "agenda", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Vote> votes;

    public boolean alreadyStarted() {
        return startedAt != null;
    }

    public boolean isOpen() {
        val now = LocalDateTime.now();
        return now.isAfter(startedAt) && now.isBefore(closedAt);
    }
}
