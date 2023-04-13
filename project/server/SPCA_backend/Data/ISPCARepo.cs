using SPCA_backend.Dtos;
using SPCA_backend.Model;

namespace SPCA_backend.Data
{
    public interface ISPCARepo
    {
        // User Methods
        public bool ValidLoginAdmin(string username, string passwordSha256Hash);
        public bool ValidLoginVets(string username, string passwordSha256Hash);
        public bool ValidLoginVolunteers(string username, string passwordSha256Hash);
        public bool AddNewUser(UserInDto userLoginInDto);
        public UserOutDto GetUserInfo(string username);
        public bool DeleteUser(int userId);
        public bool EditExistingUserAccessLevel(UserAccessInDto userAccessInDto);
        public IEnumerable<UserOutDto> GetAllUsers();

        //Dog Methods
        public bool AddNewDog(DogInDTO dogInfo);
        public bool DeleteDog(int dogId);
        public IEnumerable<DogOutDTO> ListAllDogsAllCentres();
    }
}
