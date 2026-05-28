package com.unisales.piemanager.bootstrap.service;

import com.unisales.piemanager.avaliacao.AvaliacaoRepository;
import com.unisales.piemanager.avaliacao.model.Avaliacao;
import com.unisales.piemanager.bootstrap.dto.BootstrapResponse;
import com.unisales.piemanager.curso.CursoRepository;
import com.unisales.piemanager.curso.model.Curso;
import com.unisales.piemanager.disciplina.DisciplinaRepository;
import com.unisales.piemanager.disciplina.model.Disciplina;
import com.unisales.piemanager.local.LocalRepository;
import com.unisales.piemanager.local.model.Local;
import com.unisales.piemanager.projeto.ProjetoRepository;
import com.unisales.piemanager.projeto.model.Projeto;
import com.unisales.piemanager.semestre.SemestreRepository;
import com.unisales.piemanager.semestre.model.Semestre;
import com.unisales.piemanager.turma.TurmaRepository;
import com.unisales.piemanager.turma.model.Turma;
import com.unisales.piemanager.user.UserRepository;
import com.unisales.piemanager.user.UserService;
import com.unisales.piemanager.user.dto.UserCreateRequest;
import com.unisales.piemanager.user.dto.UserResponse;
import com.unisales.piemanager.user.model.Profile;
import com.unisales.piemanager.user.model.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BootstrapService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@unisales.br";
    private static final String ADMIN_PASSWORD = "admin@123";
    private static final String DEFAULT_PASSWORD = "12345";

    private final UserRepository userRepository;
    private final UserService userService;
    private final SemestreRepository semestreRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TurmaRepository turmaRepository;
    private final LocalRepository localRepository;
    private final ProjetoRepository projetoRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public BootstrapService(UserRepository userRepository,
                            UserService userService,
                            SemestreRepository semestreRepository,
                            CursoRepository cursoRepository,
                            DisciplinaRepository disciplinaRepository,
                            TurmaRepository turmaRepository,
                            LocalRepository localRepository,
                            ProjetoRepository projetoRepository,
                            AvaliacaoRepository avaliacaoRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.semestreRepository = semestreRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.turmaRepository = turmaRepository;
        this.localRepository = localRepository;
        this.projetoRepository = projetoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Transactional
    public BootstrapResponse initialize() {
        boolean adminCreated = false;
        int usersSeeded = 0;
        int semestresSeeded = 0;
        int cursosSeeded = 0;
        int disciplinasSeeded = 0;
        int turmasSeeded = 0;
        int locaisSeeded = 0;
        int projetosSeeded = 0;
        int avaliacoesSeeded = 0;

        User admin = userRepository.findByEmail(ADMIN_EMAIL).orElse(null);
        if (admin == null) {
            UserCreateRequest request = new UserCreateRequest();
            request.setUsername(ADMIN_USERNAME);
            request.setEmail(ADMIN_EMAIL);
            request.setPassword(ADMIN_PASSWORD);
            request.setProfile(Profile.ADMIN);
            UserResponse created = userService.create(request, "bootstrap");
            admin = userRepository.findById(created.getId()).orElseThrow();
            adminCreated = true;
            usersSeeded++;
        }

        boolean missingCoordenador = userRepository.findByEmail("coordenador@unisales.br").isEmpty();
        User coordenador = ensureUser("coordenador", "coordenador@unisales.br", Profile.COORDENADOR);
        if (missingCoordenador) {
            usersSeeded++;
        }

        boolean missingProfessorComputacao = userRepository.findByEmail("prof.computacao@unisales.br").isEmpty();
        User professorComputacao = ensureUser("prof.computacao", "prof.computacao@unisales.br", Profile.PROFESSOR);
        if (missingProfessorComputacao) {
            usersSeeded++;
        }

        boolean missingProfessorEngenharia = userRepository.findByEmail("prof.engenharia@unisales.br").isEmpty();
        User professorEngenharia = ensureUser("prof.engenharia", "prof.engenharia@unisales.br", Profile.PROFESSOR);
        if (missingProfessorEngenharia) {
            usersSeeded++;
        }

        boolean missingAlunoUm = userRepository.findByEmail("aluno.um@unisales.br").isEmpty();
        User alunoUm = ensureUser("aluno.um", "aluno.um@unisales.br", Profile.ALUNO);
        if (missingAlunoUm) {
            usersSeeded++;
        }

        boolean missingAlunoDois = userRepository.findByEmail("aluno.dois@unisales.br").isEmpty();
        User alunoDois = ensureUser("aluno.dois", "aluno.dois@unisales.br", Profile.ALUNO);
        if (missingAlunoDois) {
            usersSeeded++;
        }

        boolean missingAlunoTres = userRepository.findByEmail("aluno.tres@unisales.br").isEmpty();
        User alunoTres = ensureUser("aluno.tres", "aluno.tres@unisales.br", Profile.ALUNO);
        if (missingAlunoTres) {
            usersSeeded++;
        }

        boolean missingAvaliadorExterno = userRepository.findByEmail("avaliador.externo@unisales.br").isEmpty();
        User avaliadorExterno = ensureUser("avaliador.externo", "avaliador.externo@unisales.br", Profile.AVALIADOR_EXTERNO);
        if (missingAvaliadorExterno) {
            usersSeeded++;
        }

        Semestre semestre20261 = semestreRepository.findByNomeIgnoreCase("2026/1").orElse(null);
        if (semestre20261 == null) {
            semestre20261 = new Semestre();
            semestre20261.setNome("2026/1");
            semestre20261.setDataInicio(LocalDate.of(2026, 2, 1));
            semestre20261.setDataFim(LocalDate.of(2026, 6, 30));
            semestre20261.setCreatedBy("bootstrap");
            semestre20261.setUpdatedBy("bootstrap");
            semestre20261 = semestreRepository.save(semestre20261);
            semestresSeeded++;
        }

        Curso computacao = cursoRepository.findByNomeIgnoreCase("Computacao").orElse(null);
        if (computacao == null) {
            computacao = new Curso();
            computacao.setNome("Computacao");
            computacao.setCoordenador(coordenador);
            computacao.setProfessores(new LinkedHashSet<>(Set.of(professorComputacao, professorEngenharia)));
            computacao.setCreatedBy("bootstrap");
            computacao.setUpdatedBy("bootstrap");
            computacao = cursoRepository.save(computacao);
            cursosSeeded++;
        }

        Curso engenharia = cursoRepository.findByNomeIgnoreCase("Engenharia").orElse(null);
        if (engenharia == null) {
            engenharia = new Curso();
            engenharia.setNome("Engenharia");
            engenharia.setCoordenador(coordenador);
            engenharia.setProfessores(new LinkedHashSet<>(Set.of(professorEngenharia)));
            engenharia.setCreatedBy("bootstrap");
            engenharia.setUpdatedBy("bootstrap");
            engenharia = cursoRepository.save(engenharia);
            cursosSeeded++;
        }

        Curso administracao = cursoRepository.findByNomeIgnoreCase("Administracao").orElse(null);
        if (administracao == null) {
            administracao = new Curso();
            administracao.setNome("Administracao");
            administracao.setCoordenador(coordenador);
            administracao.setProfessores(new LinkedHashSet<>(Set.of(professorComputacao)));
            administracao.setCreatedBy("bootstrap");
            administracao.setUpdatedBy("bootstrap");
            administracao = cursoRepository.save(administracao);
            cursosSeeded++;
        }

        Disciplina estatistica = disciplinaRepository.findByNomeIgnoreCaseAndCursoId("Estatistica", computacao.getId())
                .orElse(null);
        if (estatistica == null) {
            estatistica = new Disciplina();
            estatistica.setNome("Estatistica");
            estatistica.setCurso(computacao);
            estatistica.setCreatedBy("bootstrap");
            estatistica.setUpdatedBy("bootstrap");
            estatistica = disciplinaRepository.save(estatistica);
            disciplinasSeeded++;
        }

        Disciplina calculo = disciplinaRepository.findByNomeIgnoreCaseAndCursoId("Calculo", engenharia.getId())
                .orElse(null);
        if (calculo == null) {
            calculo = new Disciplina();
            calculo.setNome("Calculo");
            calculo.setCurso(engenharia);
            calculo.setCreatedBy("bootstrap");
            calculo.setUpdatedBy("bootstrap");
            calculo = disciplinaRepository.save(calculo);
            disciplinasSeeded++;
        }

        Turma turma = turmaRepository.findByNomeIgnoreCaseAndSemestreId("T1-Estatistica", semestre20261.getId())
                .orElse(null);
        if (turma == null) {
            turma = new Turma();
            turma.setNome("T1-Estatistica");
            turma.setSemestre(semestre20261);
            turma.setDisciplina(estatistica);
            turma.setCursos(new LinkedHashSet<>(Set.of(computacao, engenharia, administracao)));
            turma.setProfessores(new LinkedHashSet<>(Set.of(professorComputacao, professorEngenharia)));
            turma.setAlunos(new LinkedHashSet<>(Set.of(alunoUm, alunoDois, alunoTres)));
            turma.setCreatedBy("bootstrap");
            turma.setUpdatedBy("bootstrap");
            turma = turmaRepository.save(turma);
            turmasSeeded++;
        } else {
            turma.getProfessores().add(professorComputacao);
            turma.getProfessores().add(professorEngenharia);
            turma.getAlunos().add(alunoUm);
            turma.getAlunos().add(alunoDois);
            turma.getAlunos().add(alunoTres);
            turma.setUpdatedBy("bootstrap");
            turma = turmaRepository.save(turma);
        }

        Local localA01 = localRepository.findByNumeroIgnoreCase("A01").orElse(null);
        if (localA01 == null) {
            localA01 = new Local();
            localA01.setNumero("A01");
            localA01.setCreatedBy("bootstrap");
            localA01.setUpdatedBy("bootstrap");
            localA01 = localRepository.save(localA01);
            locaisSeeded++;
        }

        Local localA02 = localRepository.findByNumeroIgnoreCase("A02").orElse(null);
        if (localA02 == null) {
            localA02 = new Local();
            localA02.setNumero("A02");
            localA02.setCreatedBy("bootstrap");
            localA02.setUpdatedBy("bootstrap");
            localA02 = localRepository.save(localA02);
            locaisSeeded++;
        }

        Projeto projeto = projetoRepository.findByNomeIgnoreCaseAndTurmaIdAndSemestreId(
                "PIE Manager", turma.getId(), semestre20261.getId()).orElse(null);
        if (projeto == null) {
            projeto = new Projeto();
            projeto.setNome("PIE Manager");
            projeto.setDescricao("Sistema de controle de projetos de integracao academica.");
            projeto.setTurma(turma);
            projeto.setSemestre(semestre20261);
            projeto.setProfessorOrientador(professorComputacao);
            projeto.setIntegrantes(new LinkedHashSet<>(Set.of(alunoUm, alunoDois, alunoTres)));
            projeto.setLocal(localA01);
            projeto.setHorarioInicio(Instant.parse("2026-06-20T18:00:00Z"));
            projeto.setHorarioFim(Instant.parse("2026-06-20T19:00:00Z"));
            projeto.setCreatedBy("bootstrap");
            projeto.setUpdatedBy("bootstrap");
            projeto = projetoRepository.save(projeto);
            projetosSeeded++;
        }

        avaliacoesSeeded += ensureAvaliacao(projeto, professorComputacao, new BigDecimal("8.50"),
                "Bom dominio tecnico e organizacao do grupo.");
        avaliacoesSeeded += ensureAvaliacao(projeto, professorEngenharia, new BigDecimal("9.00"),
                "Excelente apresentacao e aderencia ao escopo.");
        avaliacoesSeeded += ensureAvaliacao(projeto, avaliadorExterno, new BigDecimal("8.80"),
                "Projeto consistente e aplicavel ao contexto real.");

        return new BootstrapResponse(
                adminCreated,
                userService.toResponse(admin),
                usersSeeded,
                semestresSeeded,
                cursosSeeded,
                disciplinasSeeded,
                turmasSeeded,
                locaisSeeded,
                projetosSeeded,
                avaliacoesSeeded
        );
    }

    private int ensureAvaliacao(Projeto projeto, User avaliador, BigDecimal nota, String comentario) {
        if (avaliacaoRepository.existsByProjetoIdAndAvaliadorId(projeto.getId(), avaliador.getId())) {
            return 0;
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setProjeto(projeto);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setCreatedBy("bootstrap");
        avaliacao.setUpdatedBy("bootstrap");
        avaliacaoRepository.save(avaliacao);
        return 1;
    }

    private User ensureUser(String username, String email, Profile profile) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserCreateRequest request = new UserCreateRequest();
                    request.setUsername(username);
                    request.setEmail(email);
                    request.setPassword(DEFAULT_PASSWORD);
                    request.setProfile(profile);
                    UserResponse created = userService.create(request, "bootstrap");
                    return userRepository.findById(created.getId()).orElseThrow();
                });
    }
}
