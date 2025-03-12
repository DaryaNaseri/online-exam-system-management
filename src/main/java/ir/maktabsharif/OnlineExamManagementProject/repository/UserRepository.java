package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.role = :searchedRole")
    List<User> findByRole(@Param("searchedRole") UserRole searchedRole);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchedWord, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchedWord, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchedWord, '%'))")
    List<User> findBySearchedWord(@Param("searchedWord") String searchedWord);


    @Query("SELECT u FROM User u WHERE " +
            "( u.role = :role) AND " +
            " (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchedWord, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchedWord, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchedWord, '%')))")
    List<User> filterByRoleAndSearchedWord(@Param("role") UserRole role, @Param("searchedWord") String searchedWord);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    }

