package com.kvy.demogerenciamentoaulas.service;

import com.kvy.demogerenciamentoaulas.entity.Horario;
import com.kvy.demogerenciamentoaulas.entity.Perfil;
import com.kvy.demogerenciamentoaulas.exception.HorarioEntityNotFoundException;
import com.kvy.demogerenciamentoaulas.exception.HorarioUniqueViolationException;
import com.kvy.demogerenciamentoaulas.exception.TipoSalaUniqueViolationException;
import com.kvy.demogerenciamentoaulas.repository.HorarioRepository;
import com.kvy.demogerenciamentoaulas.web.dto.HorarioDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioDTO convertToDTO(Horario horario) {
        return new HorarioDTO(horario.getId(), horario.getHoraInicio(), horario.getHoraTermino());
    }

    public Horario convertToEntity(HorarioDTO horarioDTO) {
        Horario horario = new Horario();
        horario.setId(horarioDTO.getId());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraTermino(horarioDTO.getHoraTermino());
        return horario;
    }

    @Transactional
    public Horario salvar(HorarioDTO horario) {
        if (horario.getHoraInicio() == null) {
            throw new IllegalArgumentException("A hora de início é obrigatória");
        }

        if (horario.getHoraTermino() == null) {
            throw new IllegalArgumentException("A hora de fim é obrigatória");
        }
        try {
            Horario horario1 = convertToEntity(horario);
            return horarioRepository.save(horario1);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new HorarioUniqueViolationException("Horario já cadastrado");
        }

    }

    @Transactional(readOnly = true)
    public Horario buscarPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioEntityNotFoundException(String.format("Horario id=%s não encontrado", id)));
    }

    @Transactional
    public Horario editar(Long id, Horario horario) {
        Horario existingHorario = buscarPorId(id);
        existingHorario.setHoraInicio(horario.getHoraInicio());
        existingHorario.setHoraTermino(horario.getHoraTermino());
        return horarioRepository.save(existingHorario);
    }

    @Transactional
    public void excluir(Long id) {
        Horario optionalHorario = buscarPorId(id);
        horarioRepository.delete(optionalHorario);
        System.out.println("Horario deletado com sucesso!");
    }

    @Transactional(readOnly = true)
    public List<Horario> buscarTodos() {
        return horarioRepository.findAll();
    }

    @PostConstruct
    @Transactional
    public void adicionarHorarioPadrao() {
        adicionarHorarioSeNaoExistir(LocalTime.of(8, 0), LocalTime.of(12, 0));  // Exemplo de horário
        /*adicionarHorarioSeNaoExistir(LocalTime.of(13, 0), LocalTime.of(17, 0)); // Exemplo de horário
        adicionarHorarioSeNaoExistir(LocalTime.of(18, 0), LocalTime.of(22, 0)); // Exemplo de horário*/
    }

    private void adicionarHorarioSeNaoExistir(LocalTime horaInicio, LocalTime horaTermino) {
        if (!horarioRepository.existsByHoraInicioAndHoraTermino(horaInicio, horaTermino)) {
            Horario horario = new Horario();
            horario.setHoraInicio(horaInicio);
            horario.setHoraTermino(horaTermino);
            horarioRepository.save(horario);
        }
    }
}