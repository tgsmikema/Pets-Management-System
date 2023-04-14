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
        public int getUserIdFromUserName(string username);
        public bool AddNewUser(UserInDto userLoginInDto);
        public UserOutDto GetUserInfo(string username);
        public bool DeleteUser(int userId);
        public bool EditExistingUserAccessLevel(UserAccessInDto userAccessInDto);
        public IEnumerable<UserOutDto> GetAllUsers();
        public bool ChangePasswordForCurrentUser(int userId, UserChangePasswordInDto userChangePasswordInDto);

        //Dog Methods
        public bool AddNewDog(DogInDTO dogInfo);
        public bool DeleteDog(int dogId);
        public IEnumerable<DogOutDTO> ListAllDogsAllCentres();
        public IEnumerable<DogOutDTO> ListAllDogsInACentre(int centreId);
        public DogOutDTO GetDogInformationAllCentres(int dogId);
        public DogOutDTO GetDogInformationOwnCentre(int dogId, int userCentreId);
        public bool EditDogInformation(DogEditInDto dogEditInDto);
        public bool toggleDogFlag(int dogId);
        public bool toggleDogAlert(int dogId);
        public bool addNewRequest(RequestInDto requestInDto);

        //Util Methods
        public bool AddNewScale(ScaleInDTO scale);
        public bool DeleteScale(int scaleId);
        public IEnumerable<Scale> ListAllScales();
        public bool AddNewCentre(string name);
        public bool DeleteCentre(int centreId);
        public IEnumerable<Centre> ListAllCentres();
    }
}
