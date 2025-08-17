package nghia_le.user_features.interfaces.repositories;
import nghia_le.user_features.models.User;
import java.util.List;

public interface  IUserRepository {
    List<User> findAll();
    User findByEmail(String id);
    User create(User user);
    User update(User user);
    boolean delete(String id);
}
