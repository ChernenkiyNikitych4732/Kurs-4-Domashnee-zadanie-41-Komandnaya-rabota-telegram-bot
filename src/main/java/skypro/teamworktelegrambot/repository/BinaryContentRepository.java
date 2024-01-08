package skypro.teamworktelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skypro.teamworktelegrambot.entity.BinaryContent;

/**
 * Интерфейс для доступа к данным фотографии BinaryContent в БД.
 */
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Integer> {
}