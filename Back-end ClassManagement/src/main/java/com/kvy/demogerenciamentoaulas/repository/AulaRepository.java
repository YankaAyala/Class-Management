package com.kvy.demogerenciamentoaulas.repository;

import com.kvy.demogerenciamentoaulas.entity.Aula;
import com.kvy.demogerenciamentoaulas.repository.Projection.AulaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    @Query("SELECT a.id AS id, " +
            "d.id AS disciplinaId, d.nome AS disciplinaNome, " +
            "s.id AS salaId, ts.tipoSala AS tipoSalaNome, s.numero AS salaNumero, " +
            "h.id AS horarioId, h.horaInicio AS horarioInicio, h.horaTermino AS horarioTermino, " +
            "ds.id AS diasDaSemanaId, ds.dia AS diaSemanaNome, " +
            "t.id AS turmaId, t.nome AS turmaNome " +
            "FROM Aula a " +
            "JOIN a.disciplina d " +
            "JOIN a.sala s " +
            "JOIN s.tipoSala ts " +
            "JOIN a.horario h " +
            "JOIN a.turma t "+
            "JOIN a.diaSemana ds")
    List<AulaProjection> findAllAulasWithDisciplinaNomeAndHorario();

    @Query("SELECT a.id AS id, " +
            "d.id AS disciplinaId, d.nome AS disciplinaNome, " +
            "s.id AS salaId, ts.tipoSala AS tipoSalaNome, s.numero AS salaNumero, " +
            "h.id AS horarioId, h.horaInicio AS horarioInicio, h.horaTermino AS horarioTermino, " +
            "ds.id AS diasDaSemanaId, ds.dia AS diaSemanaNome, " +
            "t.id AS turmaId, t.nome AS turmaNome " +
            "FROM Aula a " +
            "JOIN a.disciplina d " +
            "JOIN a.sala s " +
            "JOIN s.tipoSala ts " +
            "JOIN a.horario h " +
            "JOIN a.turma t " +
            "JOIN a.diaSemana ds " +
            "WHERE ds.dia = :diaSemanaDia")
    List<AulaProjection> findByDiaSemanaDia(@Param("diaSemanaDia") String diaSemanaDia);
}