package module01.question24.profiles.activation.vm.property.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("file")
public class FileStoreFinancialDataDao implements FinancialDataDao {
}
