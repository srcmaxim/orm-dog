import org.dog.QueryIT;
import org.dog.TransactionIT;
import org.dog.db.mapper.TableMapperTest;
import org.dog.db.statement.ColumnTest;
import org.dog.db.statement.TableTest;
import org.dog.util.*;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
//@Categories.ExcludeCategory({Integration.class})
@Suite.SuiteClasses({TableMapperTest.class, ColumnTest.class, TableTest.class, DataParamTest.class,
        DataTest.class, PrimitivesParamTest.class, PrimitivesTest.class, ReflectionTest.class,
        SQLPrinterTest.class, TransactionIT.class, CloserTest.class, QueryIT.class})
public class AllTests {
}
