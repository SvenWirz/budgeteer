package org.wickedsource.budgeteer.service.project;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.boot.BudgeteerBooter;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BudgeteerBooter.class})
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCreateProject() {
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(createProjectEntity());
        when(userRepository.findOne(anyLong())).thenReturn(createUserWithProjects());
        ProjectBaseData project = projectService.createProject("MyProject", 1l);
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
        Assert.assertEquals("name", project.getName());
    }

    @Test
    public void testGetProjectsForUser() {
        when(userRepository.findOne(1l)).thenReturn(createUserWithProjects());
        List<ProjectBaseData> projects = projectService.getProjectsForUser(1l);
        Assert.assertEquals(2, projects.size());
    }

    private UserEntity createUserWithProjects() {
        UserEntity user = new UserEntity();
        user.setId(1l);
        user.setName("user");
        user.setPassword("password");
        user.getAuthorizedProjects().add(createProjectEntity());
        user.getAuthorizedProjects().add(createProjectEntity());
        return user;
    }

    private ProjectEntity createProjectEntity() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1l);
        project.setName("name");
        return project;
    }

    @Test
    public void testDeleteProject() {
        projectService.deleteProject(1l);
        verify(projectRepository, times(1)).delete(1l);
    }
}
