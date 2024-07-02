package pl.npesystem.services.tables.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.npesystem.data.Role;
import pl.npesystem.data.entities.TestEntity;
import pl.npesystem.data.entities.TestSecondEntity;
import pl.npesystem.data.repositories.TestEntityRepository;
import pl.npesystem.models.dto.FilterRequestDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(initializers = DataFilterServiceTest.Initializer.class)
class DataFilterServiceTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreDBContainer.getUsername(),
                    "spring.datasource.password=" + postgreDBContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Container
    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    TestEntityRepository testEntityRepository;

    @Mock
    ApplicationContext applicationContext;


    private DataFilterService service;

    @Autowired
    private TestEntityManager manager;


    @BeforeEach
    void setUp() {
        when(applicationContext.getBean(TestEntityRepository.class)).thenReturn(testEntityRepository);

        service = new DataFilterService(applicationContext);

        TestEntity entity1 = new TestEntity();
        entity1.setStringValue("entity1");
        entity1.setBigDecimalValue(new BigDecimal("100.00"));
        entity1.setABooleanValue(true);
        entity1.setIntValue(1);
        entity1.setLongValue(1L);
        entity1.setEnumValue(Role.ADMIN);
        TestSecondEntity secondEntity1a = new TestSecondEntity();
        secondEntity1a.setStringValue("secondEntity1a");
        TestSecondEntity secondEntity1b = new TestSecondEntity();
        secondEntity1b.setStringValue("secondEntity1b");
        TestSecondEntity secondEntity1c = new TestSecondEntity();
        secondEntity1c.setStringValue("secondEntity1c");
        TestSecondEntity secondEntity1d = new TestSecondEntity();
        secondEntity1d.setStringValue("secondEntity1d");

        entity1.setOneToOne(secondEntity1a);
        entity1.setOneToMany(Set.of(secondEntity1b, secondEntity1c));
        entity1.setManyToOne(secondEntity1d);
        entity1.setManyToMany(Set.of(secondEntity1a, secondEntity1b));



        TestEntity entity2 = new TestEntity();
        entity2.setStringValue("entity2");
        entity2.setBigDecimalValue(new BigDecimal("200.00"));
        entity2.setABooleanValue(false);
        entity2.setIntValue(2);
        entity2.setLongValue(2L);
        entity2.setEnumValue(Role.USER);
        TestSecondEntity secondEntity2a = new TestSecondEntity();
        secondEntity2a.setStringValue("secondEntity2a");
        TestSecondEntity secondEntity2b = new TestSecondEntity();
        secondEntity2b.setStringValue("secondEntity2b");
        TestSecondEntity secondEntity2c = new TestSecondEntity();
        secondEntity2c.setStringValue("secondEntity2c");
        TestSecondEntity secondEntity2d = new TestSecondEntity();
        secondEntity2d.setStringValue("secondEntity2d");

        entity2.setOneToOne(secondEntity2a);
        entity2.setOneToMany(Set.of(secondEntity2b, secondEntity2c));
        entity2.setManyToOne(secondEntity2d);
        entity2.setManyToMany(Set.of(secondEntity2a, secondEntity2b));



        TestEntity entity3 = new TestEntity();
        entity3.setStringValue(null);
        entity3.setBigDecimalValue(null);
        entity3.setABooleanValue(null);
        entity3.setIntValue(null);
        entity3.setLongValue(null);
        entity3.setEnumValue(null);

        manager.merge(entity1);
        manager.merge(entity2);
        manager.merge(entity3);

    }

    private FilterRequestDTO createFilterRequest(String entityName,
                                                 FilterRequestDTO. Operation operation,
                                                 String fieldName,
                                                 Object... values) {

        FilterRequestDTO request = new FilterRequestDTO();
        request.setEntityName(entityName);

        FilterRequestDTO.FilterCriteriaDTO criteria =
                new FilterRequestDTO.FilterCriteriaDTO();
        criteria.setOperation(operation);
        criteria.setFieldName(fieldName);
        criteria.setValues(Arrays.asList(values));


        request.setFilters(Collections.singletonList(criteria));

        FilterRequestDTO.PageRequest pageRequest = new FilterRequestDTO.PageRequest(0,10,
                new FilterRequestDTO.Sort(FilterRequestDTO.Direction.ASC, "id"));
        request.setPageRequest(pageRequest);
        return request;
    }

    @Test
    void testEqualStringFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing


        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "stringValue",
                "entity1");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testEqualBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "bigDecimalValue",
                new BigDecimal("100.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testEqualBoolean() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "aBooleanValue",
                true);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testEqualInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "intValue",
                1);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testEqualLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "longValue",
                1L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testEqualEnum() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "enumValue",
                Role.ADMIN);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testNotEqualStringFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_EQUALS,
                "stringValue",
                "entity1");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2", null);
    }

    @Test
    void testNotEqualBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_EQUALS,
                "bigDecimalValue",
                new BigDecimal("100.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2", null);
    }

    @Test
    void testNotEqualBoolean() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_EQUALS,
                "aBooleanValue",
                true);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2", null);
    }

    @Test
    void testNotEqualInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_EQUALS,
                "intValue",
                1);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2", null);
    }

    @Test
    void testNotEqualLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_EQUALS,
                "longValue",
                1L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2", null);
    }

    @Test
    void testNotEqualEnum() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_EQUALS,
                "enumValue",
                Role.ADMIN);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2", null);
    }

    @Test
    void testGreaterThanBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.GREATER_THAN,
                "bigDecimalValue",
                new BigDecimal("100.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2");
    }

    @Test
    void testGreaterThanInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.GREATER_THAN,
                "intValue",
                1);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2");
    }

    @Test
    void testGreaterThanLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.GREATER_THAN,
                "longValue",
                1L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity2");
    }

    @Test
    void testLessThanBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LESS_THAN,
                "bigDecimalValue",
                new BigDecimal("200.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testLessThanInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LESS_THAN,
                "intValue",
                2);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testLessThanLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LESS_THAN,
                "longValue",
                2L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testGreaterOrEqualBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.GREATER_THAN_EQUAL_TO,
                "bigDecimalValue",
                new BigDecimal("100.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testGreaterOrEqualInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.GREATER_THAN_EQUAL_TO,
                "intValue",
                1);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testGreaterOrEqualLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.GREATER_THAN_EQUAL_TO,
                "longValue",
                1L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testLessOrEqualBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LESS_THAN_EQUAL_TO,
                "bigDecimalValue",
                new BigDecimal("200.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testLessOrEqualInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LESS_THAN_EQUAL_TO,
                "intValue",
                2);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testLessOrEqualLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LESS_THAN_EQUAL_TO,
                "longValue",
                2L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testBetweenBigDecimal() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.BETWEEN,
                "bigDecimalValue",
                new BigDecimal("100.00"),
                new BigDecimal("200.00"));

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testBetweenInt() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.BETWEEN,
                "intValue",
                1,
                2);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testBetweenLong() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.BETWEEN,
                "longValue",
                1L,
                2L);

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testLikeStringFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.LIKE,
                "stringValue",
                "entity%");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testNotLikeStringFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_LIKE,
                "stringValue",
                "entity%");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results).hasSize(1);
        results.forEach(result -> assertThat(result.getStringValue()).isNull());
    }

    @Test
    void testNullStringFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NULL,
                "stringValue");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results).hasSize(1);
        results.forEach(result -> assertThat(result.getStringValue()).isNull());
    }

    @Test
    void testNotNullStringFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.NOT_NULL,
                "stringValue");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(2)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1", "entity2");
    }

    @Test
    void testInvalidEntityName() {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("InvalidEntity",
                FilterRequestDTO.Operation.EQUALS,
                "stringValue",
                "entity1");

        // When
        ClassNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(ClassNotFoundException.class, () -> {
            service.getFilteredEntity(request);
        });

        // Then
        assertThat(exception.getMessage())
                .isEqualTo("pl.npesystem.data.entities.InvalidEntity");
    }

//    @Test
//    void testInvalidFieldName() {
//        // Given - setup initial DB data for testing
//
//        FilterRequestDTO request = createFilterRequest("TestEntity",
//                FilterRequestDTO.Operation.EQUALS,
//                "invalidField",
//                "entity1");
//
//        // When
//        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            service.getFilteredEntity(request, TestEntity.class);
//        });
//
//        // Then
//        assertThat(exception.getMessage())
//                .isEqualTo("Field invalidField not found");
//    }

    @Test
    void testOneToOneEqualsFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing

        FilterRequestDTO request = createFilterRequest("TestEntity",
                FilterRequestDTO.Operation.EQUALS,
                "oneToOne.stringValue",
                "secondEntity1a");

        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);

        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

    @Test
    void testOneToManyInFilter() throws ClassNotFoundException {
        // Given - setup initial DB data for testing
        String entityName = "TestEntity";
        FilterRequestDTO.Operation operation = FilterRequestDTO.Operation.MEMBER;
        String filterField = "oneToMany.stringValue";
        String fieldValue1 = "secondEntity1b";
        String fieldValue2 = "secondEntity1c";

        FilterRequestDTO request = createFilterRequest(entityName,
                operation,
                filterField,
                fieldValue1, fieldValue2);
        // When
        List<TestEntity> results = service.getFilteredEntity(request, TestEntity.class);
        // Then
        assertThat(results)
                .hasSize(1)
                .extracting(TestEntity::getStringValue)
                .containsOnly("entity1");
    }

}