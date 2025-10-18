public class PaqueteDeluxe extends PaqueteTiquetes {
    private  List<String> beneficios = new ArrayList<>();
    private  List<Tiquete> cortesias = new ArrayList<>(); 

    public PaqueteDeluxe(Collection<String> beneficiosIniciales,
                         Collection<Tiquete> tiquetesIncluidosIniciales) {
        super(false); 
        if (beneficiosIniciales != null) beneficios.addAll(beneficiosIniciales);
        if (tiquetesIncluidosIniciales != null) {
            for (Tiquete t : tiquetesIncluidosIniciales) super.agregarTiquete(t);
        }
    }

    public List<String> getBeneficios() { return Collections.unmodifiableList(beneficios); }
    public void agregarBeneficio(String b) { beneficios.add(Objects.requireNonNull(b)); }

    public List<Tiquete> getCortesias() { return Collections.unmodifiableList(cortesias); }
    public void agregarCortesia(Tiquete t) { cortesias.add(Objects.requireNonNull(t)); }

    public List<Tiquete> todosLosAccesos() {
        List<Tiquete> all = new ArrayList<>(getTiquetes());
        all.addAll(cortesias);
        return Collections.unmodifiableList(all);
    }

    @Override public void agregarTiquete(Tiquete tiquete) {
        super.agregarTiquete(tiquete);
    }
}