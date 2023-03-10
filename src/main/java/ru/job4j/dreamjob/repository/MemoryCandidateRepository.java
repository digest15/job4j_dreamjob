package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Иванов", "Java 6 yeas", 0, 0));
        save(new Candidate(1, "Петров", "Java 3 yeas", 0, 0));
        save(new Candidate(2, "Сидоров", "Java 2 yeas", 0, 0));
        save(new Candidate(3, "Баширов", "Like Salisbury", 0, 0));
        save(new Candidate(4, "Чипига", "Like spires", 0, 0));
        save(new Candidate(5, "Арсентьев", "Like job4j", 0, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                new Candidate(
                        oldVacancy.getId(),
                        candidate.getName(),
                        candidate.getDescription(),
                        candidate.getCityId(),
                        candidate.getFileId()
                )
        ) != null;
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
