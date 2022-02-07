package com.sicredi.votingsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sicredi.votingsystem.enums.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "en_vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "option", nullable = false, updatable = false)
    private VoteOption option;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false, updatable = false)
    @JsonBackReference
    private VotingAgenda agenda;

}
