package id.zitech.base.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import id.zitech.base.TestUtil;
import id.zitech.base.service.dto.BookDTO;
import io.quarkus.test.junit.QuarkusTest;
import redis.embedded.RedisServer;
import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import io.quarkus.liquibase.LiquibaseFactory;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;

@QuarkusTest
public class BookResourceTest {

    private static final TypeRef<BookDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<BookDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ISBN = 1;
    private static final Integer UPDATED_ISBN = 2;

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    static RedisServer server;

    String adminToken;

    BookDTO bookDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config = RestAssured.config()
                .objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void startRedisServer() throws IOException {
        server = new RedisServer(6379);
        server.start();
    }

    @AfterAll
    public static void stopRedisServer() {
        server.stop();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it, if
     * they test an entity which requires the current entity.
     */
    public static BookDTO createEntity() {
        var bookDTO = new BookDTO();
        bookDTO.title = DEFAULT_TITLE;
        bookDTO.isbn = DEFAULT_ISBN;
        bookDTO.author = DEFAULT_AUTHOR;
        bookDTO.publisher = DEFAULT_PUBLISHER;
        bookDTO.city = DEFAULT_CITY;
        return bookDTO;
    }

    @BeforeEach
    public void initTest() {
        bookDTO = createEntity();
    }

    @Test
    public void createBook() {
        var databaseSizeBeforeCreate = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // Create the Book
        bookDTO = given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(CREATED.getStatusCode()).extract()
                .as(ENTITY_TYPE);

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(Integer.valueOf(databaseSizeBeforeCreate) + 1);
        var testBookDTO = bookDTOList.stream().filter(it -> bookDTO.id.equals(it.id)).findFirst().get();
        assertThat(testBookDTO.title).isEqualTo(DEFAULT_TITLE);
        assertThat(testBookDTO.isbn).isEqualTo(DEFAULT_ISBN);
        assertThat(testBookDTO.author).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBookDTO.publisher).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testBookDTO.city).isEqualTo(DEFAULT_CITY);
    }

    @Test
    public void createBookWithExistingId() {
        var databaseSizeBeforeCreate = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // Create the Book with an existing ID
        bookDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkIsbnIsRequired() throws Exception {
        var databaseSizeBeforeTest = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // set the field null
        bookDTO.isbn = null;

        // Create the Book, which fails.
        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAuthorIsRequired() throws Exception {
        var databaseSizeBeforeTest = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // set the field null
        bookDTO.author = null;

        // Create the Book, which fails.
        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPublisherIsRequired() throws Exception {
        var databaseSizeBeforeTest = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // set the field null
        bookDTO.publisher = null;

        // Create the Book, which fails.
        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCityIsRequired() throws Exception {
        var databaseSizeBeforeTest = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // set the field null
        bookDTO.city = null;

        // Create the Book, which fails.
        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateBook() {
        // Initialize the database
        bookDTO = given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(CREATED.getStatusCode()).extract()
                .as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // Get the book
        var updatedBookDTO = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books/{id}", bookDTO.id).then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON)
                .extract().body().as(ENTITY_TYPE);

        // Update the book
        updatedBookDTO.title = UPDATED_TITLE;
        updatedBookDTO.isbn = UPDATED_ISBN;
        updatedBookDTO.author = UPDATED_AUTHOR;
        updatedBookDTO.publisher = UPDATED_PUBLISHER;
        updatedBookDTO.city = UPDATED_CITY;

        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(updatedBookDTO).when().put("/api/books").then().statusCode(OK.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeUpdate);
        var testBookDTO = bookDTOList.stream().filter(it -> updatedBookDTO.id.equals(it.id)).findFirst().get();
        assertThat(testBookDTO.title).isEqualTo(UPDATED_TITLE);
        assertThat(testBookDTO.isbn).isEqualTo(UPDATED_ISBN);
        assertThat(testBookDTO.author).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBookDTO.publisher).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testBookDTO.city).isEqualTo(UPDATED_CITY);
    }

    @Test
    public void updateNonExistingBook() {
        var databaseSizeBeforeUpdate = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().put("/api/books").then().statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Book in the database
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBook() {
        // Initialize the database
        bookDTO = given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(CREATED.getStatusCode()).extract()
                .as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE).size();

        // Delete the book
        given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .delete("/api/books/{id}", bookDTO.id).then().statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var bookDTOList = given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books").then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON).extract()
                .as(LIST_OF_ENTITY_TYPE);

        assertThat(bookDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllBooks() {
        // Initialize the database
        bookDTO = given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(CREATED.getStatusCode()).extract()
                .as(ENTITY_TYPE);

        // Get all the bookList
        given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when().get("/api/books?sort=id,desc")
                .then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON)
                .body("id", hasItem(bookDTO.id.intValue())).body("title", hasItem(DEFAULT_TITLE))
                .body("isbn", hasItem(DEFAULT_ISBN.intValue())).body("author", hasItem(DEFAULT_AUTHOR))
                .body("publisher", hasItem(DEFAULT_PUBLISHER)).body("city", hasItem(DEFAULT_CITY));
    }

    @Test
    public void getBook() {
        // Initialize the database
        bookDTO = given().auth().preemptive().oauth2(adminToken).contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(bookDTO).when().post("/api/books").then().statusCode(CREATED.getStatusCode()).extract()
                .as(ENTITY_TYPE);

        var response = // Get the book
                given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                        .get("/api/books/{id}", bookDTO.id).then().statusCode(OK.getStatusCode())
                        .contentType(APPLICATION_JSON).extract().as(ENTITY_TYPE);

        // Get the book
        given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books/{id}", bookDTO.id).then().statusCode(OK.getStatusCode()).contentType(APPLICATION_JSON)
                .body("id", is(bookDTO.id.intValue()))

                .body("title", is(DEFAULT_TITLE)).body("isbn", is(DEFAULT_ISBN.intValue()))
                .body("author", is(DEFAULT_AUTHOR)).body("publisher", is(DEFAULT_PUBLISHER))
                .body("city", is(DEFAULT_CITY));
    }

    @Test
    public void getNonExistingBook() {
        // Get the book
        given().auth().preemptive().oauth2(adminToken).accept(APPLICATION_JSON).when()
                .get("/api/books/{id}", Long.MAX_VALUE).then().statusCode(NOT_FOUND.getStatusCode());
    }
}
