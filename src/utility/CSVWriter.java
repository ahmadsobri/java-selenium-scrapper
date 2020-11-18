package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public final class CSVWriter {

    public static void createCSV(List<ProductModel> productModels, List<String> columns, String path) {
        try (PrintWriter writer = new PrintWriter(new File(path))) {
            StringBuilder sb = new StringBuilder();

            for(String name:columns){
                sb.append(name).append(";");
            }
            sb.append('\n');

            for (ProductModel item : productModels) {
                sb.append('"')
                        .append(handleQoute(item.getNameOfProduct())).append('"').append(';').append('"')
                        .append(handleQoute(item.getDescription())).append('"').append(';')
                        .append(item.getImageLink()).append(';')
                        .append(item.getPrice()).append(';')
                        .append(item.getRating());
                sb.append('\n');
            }
            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String handleQoute(String str){
        String result = str.replaceAll("\"", "\"\"");
        return result;
    }
}
