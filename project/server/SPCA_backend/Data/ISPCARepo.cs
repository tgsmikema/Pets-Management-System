using SPCA_backend.Dtos;
using SPCA_backend.Model;

namespace SPCA_backend.Data
{
    public interface ISPCARepo
    {
        // --------------------User Methods----------------------------
        public bool ValidLoginAdmin(string username, string passwordSha256Hash);
        public bool ValidLoginVets(string username, string passwordSha256Hash);
        public bool ValidLoginVolunteers(string username, string passwordSha256Hash);
        public bool AddNewUser(UserInDto userLoginInDto);
        public UserOutDto GetUserInfo(string username);
        public bool DeleteUser(int userId);
        public IEnumerable<UserOutDto> GetAllUsers();
        public bool EditExistingUserAccessLevel(UserAccessInDto userAccessInDto);
        public int GetUserIdFromUserName(string username);
        public bool ChangePasswordForCurrentUser(int userId, UserChangePasswordInDto userChangePasswordInDto);

        //---------------------Dog Methods------------------------------
        public bool AddNewDog(DogInDTO dogInfo);
        public bool DeleteDog(int dogId);
        public IEnumerable<DogOutDTO> ListAllDogsAllCentres();
        public IEnumerable<DogOutDTO> ListAllDogsInACentre(int centreId);
        public DogOutDTO GetDogInformationAllCentres(int dogId);
        public DogOutDTO GetDogInformationOwnCentre(int dogId, int userCentreId);
        public bool EditDogInformation(DogEditInDto dogEditInDto);
        public bool ToggleDogFlag(int dogId);
        public bool ToggleDogAlert(int dogId);
        public bool AddNewRequest(RequestInDto requestInDto);
        public bool AddWeightFromScaleToRequest(ScaleWeightRequestInDto scaleWeightRequestInDto);
        public DogWeightRequestOutDto GetCurrentDogRequestWeight(int dogId);
        public bool SaveCurrentWeight(int dogId);
        public IEnumerable<Weight> GetWeightHistoryForADog(int dogId);

        //----------------------Util Methods-----------------------------
        public bool AddNewScale(ScaleInDTO scale);
        public bool DeleteScale(int scaleId);
        public IEnumerable<Scale> ListAllScales();
        public bool AddNewCentre(string name);
        public bool DeleteCentre(int centreId);
        public IEnumerable<Centre> ListAllCentres();
        public StatsOutDTO getCurrentWeekStats(int centerId);
        public IEnumerable<StatsOutDTO> getWeeklyStats(StatsInDTO statsInDTO);
        public IEnumerable<StatsOutDTO> getMonthlyStats(StatsInDTO statsInDTO);

        //-------------------------Message Methods--------------------------

        public Task AddNewMessage(MessageInDto messageInDto);
        public IEnumerable<UserOutDto> getAlreadyMessagedPeopleList(int currentUserId);
        public IEnumerable<UserOutDto> getNeverMessagedPeopleList(int currentUserId);
        public IEnumerable<MessageOutDto> getChatHistory(int currentUserId, int chatToUserId);
    }
}
