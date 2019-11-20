//using loan pattern
public class MainClassL {
    public static void main(String[] args) {
        LoanLoader.StartLoading(Loader ->
                Loader.tags("azur_lane")
                        .number(50));
    }
}