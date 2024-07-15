package com.ejemplo.catalogo_libros.controller;

import com.ejemplo.catalogo_libros.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al Catálogo de Libros");

        while (true) {
            System.out.println("Ingrese el título del libro que desea buscar:");
            String title = scanner.nextLine();

            if (title.equalsIgnoreCase("salir")) {
                break;
            }

            String result = bookService.searchAndRegisterBook(title);
            System.out.println(result);
        }

        scanner.close();
    }
}
