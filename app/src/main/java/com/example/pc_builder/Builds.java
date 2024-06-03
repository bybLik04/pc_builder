package com.example.pc_builder;

import com.google.firebase.firestore.DocumentReference;

public class Builds {
    private String title;
    private int Cost;
    private DocumentReference CPU;
    private DocumentReference GPU;
    private DocumentReference Motherboard;
    private DocumentReference PSU;
    private DocumentReference RAM;
    private DocumentReference Storage;
    private DocumentReference Cases;
    private DocumentReference Cooling;

    public Builds(){

    }
    public Builds(int cost, DocumentReference cpu, DocumentReference gpu, DocumentReference motherboard, DocumentReference psu, DocumentReference ram, DocumentReference storage, DocumentReference cases, DocumentReference cooling){
        this.Cost = cost;
        this.CPU = cpu;
        this.GPU = gpu;
        this.Motherboard = motherboard;
        this.PSU = psu;
        this.RAM = ram;
        this.Storage = storage;
        this.Cases = cases;
        this.Cooling = cooling;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getCost() {
        return Cost;
    }
    public DocumentReference getCpu() {
        return CPU;
    }
    public DocumentReference getGpu() {
        return GPU;
    }
    public DocumentReference getMotherboard() {
        return Motherboard;
    }
    public DocumentReference getPsu() {
        return PSU;
    }
    public DocumentReference getRam() {
        return RAM;
    }
    public DocumentReference getStorage() {
        return Storage;
    }
    public DocumentReference getCases() {
        return Cases;
    }
    public DocumentReference getCooling() {
        return Cooling;
    }

}
