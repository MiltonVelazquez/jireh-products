package jireh.productos.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jireh.productos.repositories.ProductRepository;

@Component
public class WeeklyResetTask {
    @Autowired
    private ProductRepository productRepository;

    // Se ejecuta cada lunes a las 00:00
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetWeeklyVisits() {
        productRepository.resetAllViews();
        System.out.println("Contador de visitas reseteado");
    }
}