package com.training.portal.model;

import javax.persistence.*;

@Entity
@Table(name = "question_answer")
public class QuesAndAns {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
