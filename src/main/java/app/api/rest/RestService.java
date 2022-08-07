package app.api.rest;

import app.helpers.HibernateHelper;
import app.model.Product;

import app.model.documents.Receipt;
import app.model.request.ReceiptRequest;
import app.model.response.HttpResponseBody;
import io.swagger.annotations.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SwaggerDefinition(
        info = @Info(
                version = "1.0",
                title = "Реализация серверной части приложения по учету товаров на складе. ",
                description = "Приложение должно включать API для просмотра, создания, редактирования и удаления сущностей\n" +
                        "На вход приложению поступают документы: API включает операции импорта (создания) и просмотра документов\n" +
                        "На выходе имеется возможность сформировать отчеты:\n" +
                        "●\t Общий список товаров (артикул, наименование, цены закупки и продажи).\n" +
                        "В качестве опционального параметра может быть передан фильтр по имени товара.\n" +
                        "●\t Остатки товаров на складах (артикул, наименование, остаток по всем складам). \n" +
                        "В качестве опционального параметра может быть передан фильтр по складу.\n" +
                        "Общение между клиентом и сервером (rest сервисом) осуществляется с помощью сообщений в формате JSON.",
                contact = @Contact(
                        name = "Anton Shatkovsiy",
                        email = "javatonka@gmail.com"
                )
        ),
        schemes = {SwaggerDefinition.Scheme.HTTP}
)
@Api(value = "api")
/**
 * на каждый запрос будет создаваться новый объект класса RestService, в отличие от обычных сервлетов, у которых создаётся один объект сервлета, который обрабатывает запросы в разных потоках
 */
@Path("/api")
public class RestService {

    @ApiOperation(value = "Доступные операции в рамках данного сервиса")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK",
                    responseHeaders = {@ResponseHeader(name = "Allow", description = "GET, POST, PUT, DELETE", response = String.class)})
    })
    @Path("")
    @OPTIONS
    public Response getAllowedMethods() {
        Response response = Response.status(Response.Status.OK)
                .allow("GET", "POST", "PUT", "DELETE")
                .build();
        return response;
    }




    @ApiOperation(
            value = "Создание отчета общего списка товаров",
            response = HttpResponseBody.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Product [product_name] already exists"),
            @ApiResponse(code = 500, message = "Exception occurred while saving product")
    })
    @POST
    @Path("/allsetproduct")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyProduct(
            @ApiParam(value = "Общий список товаров", required = true)
            ReceiptRequest requestModel
    ) {


        List<Receipt> receipts = requestModel.getReceipts();
        HttpResponseBody responseBody = new HttpResponseBody();

        if (receipts == null || receipts.isEmpty()) {
            responseBody.setMessage("Products does not specified");
            responseBody.setCode(Response.Status.BAD_REQUEST.getStatusCode());
            responseBody.setCodeMessage(Response.Status.BAD_REQUEST.getReasonPhrase());
        } else {
            Transaction transaction = null;
            try (Session session = HibernateHelper.getInstance().getFactory().openSession()) {
                transaction = session.beginTransaction();

                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Product> productNamesQuery = builder.createQuery(Product.class);
                Root<Product> productRoot = productNamesQuery.from(Product.class);
                productNamesQuery.select(productRoot);

                List<Product> productsFromDb = session.createQuery(productNamesQuery).list();
                List<String> productNames = productsFromDb
                        .stream()
                        .map(Product::getName)
                        .collect(Collectors.toList());

                StringBuilder sb = new StringBuilder();

                receipts.forEach(receipt -> {
                    if (productNames.contains(receipt.getProduct().getName())) {
                        if (receipt.getProductCount() > 0 && receipt.getProductPrice() >= 0 && receipt.getProduct().getArticle() != null) {

                            Product actualProduct = productsFromDb
                                    .stream()
                                    .filter(product -> product.getName().equals(receipt.getProduct().getName()))
                                    .findFirst()
                                    .get();
                            actualProduct.setCount(actualProduct.getCount() + receipt.getProductCount());

                            receipt.setProduct(actualProduct);

                            session.save(receipt);

                            sb.append("Receipt for product [")
                                    .append(actualProduct.getName())
                                    .append("] saved. ");
                        } else {
                            sb.append("Error: in product [")
                                    .append(receipt.getProduct().getName())
                                    .append("] purchase data incorrect: productCount must be > 0, productPrice must be >=0, productDate must be not null and have format YYYY-MM-DD. ");
                        }
                    } else {
                        sb.append("Error: product [")
                                .append(receipt.getProduct().getName())
                                .append("] does not exist. ");
                    }
                });

                responseBody.setMessage(sb.toString().trim());
                responseBody.setCode(Response.Status.OK.getStatusCode());
                responseBody.setCodeMessage(Response.Status.OK.getReasonPhrase());

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                responseBody.setMessage("Exception occurred while saving receiptes");
                responseBody.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
                responseBody.setCodeMessage(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }

        Response response = Response.status(responseBody.getCode())
                .entity(responseBody)
                .build();

        return response;
    }

}
