package skypro.teamworktelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skypro.teamworktelegrambot.entity.Report;

/**
 * Интерфейс для доступа к отчетам о животных Report в БД.
 */
public interface ReportsRepository extends JpaRepository<Report, Integer> {
}