package org.wickedsource.budgeteer.service.imports;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.boot.BudgeteerBooter;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BudgeteerBooter.class})
public class ImportServiceTest {

    @MockBean
    private ImportRepository importRepository;

    @MockBean
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private ImportService importService;

    @Test
    public void testLoadImports() {
        when(importRepository.findByProjectId(1l)).thenReturn(Arrays.asList(createImportEntity()));
        List<Import> imports = importService.loadImports(1l);
        Assert.assertEquals(1, imports.size());
        Assert.assertEquals("TestImport", imports.get(0).getImportType());
    }

    @Test
    public void testDeleteImport() {
        importService.deleteImport(1l);
        verify(importRepository, times(1)).delete(1l);
        verify(workRecordRepository, times(1)).deleteByImport(1l);
    }

    private ImportEntity createImportEntity() {
        ImportEntity entity = new ImportEntity();
        entity.setEndDate(new Date());
        entity.setStartDate(new Date());
        entity.setImportType("TestImport");
        entity.setId(1l);
        return entity;
    }

}
