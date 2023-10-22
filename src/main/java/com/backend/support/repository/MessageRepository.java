package com.backend.support.repository;

import com.backend.support.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop50ByOrderByIdDesc();

    List<Message> findAllByChatId(Long chatId);
}
