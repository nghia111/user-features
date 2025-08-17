package nghia_le.user_features.repositories;

import nghia_le.user_features.interfaces.repositories.IUserRepository;
import nghia_le.user_features.models.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserRepository implements IUserRepository {
    private final DataSource dataSource;
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRoleId(rs.getString("role_id"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all users", e);
        }
        return users;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRoleId(rs.getString("role_id"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by email", e);
        }
        return null;
    }
    @Override
    public User create(User user) {
        String sql = "INSERT INTO Users (id, name, email, password_hash, role_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getRoleId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                return user;
            } else {
                throw new RuntimeException("Failed to insert user into database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
