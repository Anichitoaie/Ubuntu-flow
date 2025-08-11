//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ApiTesting;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class HTTP_Requests {
    int id;

    @Test
    void getUsers(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("page",equalTo(2))

                .log().all();
    }
    @Test
    void create_user(){
        HashMap hm =new HashMap();
        hm.put("name","syraj");
        hm.put("job","unemployed");
        id =given()
                .contentType("application/json")
                .header("x-api-key","reqres-free-v1")
                .body(hm)
                .when()
                .post("https://reqres.in/api/users")
                .jsonPath().getInt("id");
        //.then()
        //.statusCode(201)
        //.log().all();
    }
    @Test(dependsOnMethods = {"create_user"})
    void update_user(){
        HashMap hm =new HashMap();
        hm.put("name","syraj");
        hm.put("job","alfa");
        given()
                .contentType("application/json")
                .header("x-api-key","reqres-free-v1")
                .body(hm)
                .when()
                .put("https://reqres.in/api/users/"+id)

                .then()
                .statusCode(200)
                .log().all();

    }
    @Test(dependsOnMethods = {"create_user"})
    void delete_user(){
        HashMap hm =new HashMap();
        hm.put("name","syraj");
        hm.put("job","alfa");
        given()
                .contentType("application/json")
                .header("x-api-key","reqres-free-v1")

                .when()
                .delete("https://reqres.in/api/users/2"+id)

                .then()
                .statusCode(204)
                .log().all();

    }
}

