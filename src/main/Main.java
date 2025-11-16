/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import DAO.HistoriaClinicaDAO;
import DAO.PacienteDAO;
import Models.HistoriaClinica;
import Models.Paciente;
import Service.HistoriaClinicaServiceImpl;
import Service.PacienteServiceImpl;
import Service.Service;
/**
 *
 * @author agust
 */
public class Main {
        public static void main(String[] args) {

         // 1) Crear los DAO (acceso a base de datos)
        PacienteDAO pacienteDAO = new PacienteDAO();
        HistoriaClinicaDAO historiaDAO = new HistoriaClinicaDAO();

        // 2) Crear los Service, inyectando los DAO
        Service<Paciente> pacienteService =
                new PacienteServiceImpl(pacienteDAO);
        Service<HistoriaClinica> historiaService =
                new HistoriaClinicaServiceImpl(historiaDAO);

        // 3) Crear el menú y pasarle los servicios
        AppMenu menu = new AppMenu(pacienteService, historiaService);

        // 4) Main invoca AppMenu (como pide el TP)
        menu.iniciar();
    }
}

