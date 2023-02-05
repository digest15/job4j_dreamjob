package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Иванов", "Java 6 yeas"));
        save(new Candidate(1, "Петров", "Java 3 yeas"));
        save(new Candidate(2, "Сидоров", "Java 2 yeas"));
        save(new Candidate(3, "Баширов", "Like Salisbury"));
        save(new Candidate(4, "Чипига", "Like spires"));
        save(new Candidate(5, "Арсентьев", "Like job4j"));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        this.candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldVacancy) ->
                new Candidate(oldVacancy.getId(),
                        candidate.getName(),
                        candidate.getDescription())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
