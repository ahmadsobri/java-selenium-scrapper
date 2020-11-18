import utility.CSVWriter;
import utility.ProductModel;
import utility.WebScrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrapApplication {

    public static void main(String[] args) {
        grabProduct(100);
    }

    static void grabProduct(int total) {
        List<ProductModel> productModels = new ArrayList<>();
        List<String> links = WebScrapper.getProductLinks(total);

        int n = links.size();
        for (String link : links) {
            ProductModel productModel = WebScrapper.getDetailProduct(link);
            productModels.add(productModel);
            n--;
            System.out.println("pending:"+n);
        }

        //generate csv file
        List<String> columns = new ArrayList<>(
                Arrays.asList("Name of Product","Description","Image Link","Price","Rating")
        );
        CSVWriter.createCSV(productModels, columns,"product.csv");
    }
}

