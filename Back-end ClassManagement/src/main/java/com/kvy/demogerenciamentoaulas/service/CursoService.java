package com.kvy.demogerenciamentoaulas.service;

import com.kvy.demogerenciamentoaulas.entity.Curso;
import com.kvy.demogerenciamentoaulas.entity.Disciplina;
import com.kvy.demogerenciamentoaulas.entity.Modalidade;
import com.kvy.demogerenciamentoaulas.entity.Semestre;
import com.kvy.demogerenciamentoaulas.exception.CursoEntityNotFoundException;
import com.kvy.demogerenciamentoaulas.exception.DisciplinaEntityNotFoundException;
import com.kvy.demogerenciamentoaulas.repository.CursoRepository;
import com.kvy.demogerenciamentoaulas.repository.ModalidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    private final ModalidadeRepository modalidadeRepository;

    @Transactional
    public Curso salvar(Curso curso) {

        return cursoRepository.save(curso);
    }
    @Transactional
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new CursoEntityNotFoundException(String.format("Curso id=%s não encontrado", id)));
    }
    @Transactional
    public Curso editar(Long id, Curso curso) {
        Curso existingCurso = buscarPorId(id);

        existingCurso.setCurso(curso.getCurso());
        Modalidade modalidade = modalidadeRepository.findById(curso.getModalidade().getId())
                .orElseThrow(() -> new RuntimeException("Semestre não encontrado com o ID: " + curso.getModalidade().getId()));
        existingCurso.setModalidade(modalidade);
        return existingCurso;
    }

    @Transactional
    public void excluir(Long id) {
        Optional<Curso> optionalCurso = cursoRepository.findById(id);
        if (optionalCurso.isPresent()) {
            cursoRepository.delete(optionalCurso.get());
            System.out.println("Deletado com Sucesso!");
        } else {
            throw new RuntimeException("Curso não encontrado com o ID: " + id);
        }
    }


    @Transactional(readOnly = true)
    public List<Curso> buscarTodos() {
        return cursoRepository.findAll();
    }
}
