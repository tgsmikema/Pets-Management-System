using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SPCA_backend.Dtos;
using SPCA_backend.Model;
using SPCA_backend.Handler;

namespace SPCA_backend.Data
{
    public class SPCARepo : ISPCARepo
    {
        //constants
        public static int REQUEST_EXPIRING_MINUTES = 2;


        private readonly SPCA_DBContext _dbContext;
        public SPCARepo(SPCA_DBContext dbContext)
        {
            _dbContext = dbContext;
        }

        public bool ValidLoginAdmin(string username, string passwordSha256Hash)
        {
            User userLogin = _dbContext.Users.FirstOrDefault
               (e => e.UserName == username && e.PasswordSha256Hash == passwordSha256Hash && e.UserType == "admin");

            if (userLogin == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public bool ValidLoginVets(string username, string passwordSha256Hash)
        {
            User userLogin = _dbContext.Users.FirstOrDefault
               (e => e.UserName == username && e.PasswordSha256Hash == passwordSha256Hash && e.UserType == "vet");

            if (userLogin == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public bool ValidLoginVolunteers(string username, string passwordSha256Hash)
        {
            User userLogin = _dbContext.Users.FirstOrDefault
               (e => e.UserName == username && e.PasswordSha256Hash == passwordSha256Hash && e.UserType == "volunteer");

            if (userLogin == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public bool AddNewUser(UserInDto userInDto)
        {
            User userCheck = _dbContext.Users.FirstOrDefault(e => e.UserName == userInDto.UserName);

            if (userCheck == null)
            {
                User newUser = new User
                {
                    UserName = userInDto.UserName,
                    PasswordSha256Hash = SPCAAuthHandler.getSha256Hash(userInDto.Password),
                    UserType = userInDto.UserType,
                    FirstName = userInDto.FirstName,
                    LastName = userInDto.LastName,
                    CentreId = userInDto.CentreId,
                };

                EntityEntry<User> e = _dbContext.Users.Add(newUser);
                _dbContext.SaveChanges();
                return true;
            }
            else
            {
                return false;
            }
        }

        public UserOutDto GetUserInfo(string username)
        {
            User user = _dbContext.Users.FirstOrDefault(e => e.UserName == username);
            UserOutDto userOutDto = ConvertToUserOutDTO(user);
            return userOutDto;
        }

        public bool DeleteUser(int userId)
        {
            User user = _dbContext.Users.FirstOrDefault(e => e.Id == userId);
            if (user == null)
            {
                return false;
            }
            else
            {
                _dbContext.Remove(user);
                _dbContext.SaveChanges();
                return true;
            }
        }

        public IEnumerable<UserOutDto> GetAllUsers()
        {
            List<UserOutDto> AllUsers = new List<UserOutDto>();
            IEnumerable<User> users = _dbContext.Users.ToList();
            foreach(User user in users) { AllUsers.Add(ConvertToUserOutDTO(user)); }
            return AllUsers;
        }

        public UserOutDto ConvertToUserOutDTO(User user)
        {
            UserOutDto userOutDto = new UserOutDto
            {
                Id = user.Id,
                UserName = user.UserName,
                UserType = user.UserType,
                FirstName = user.FirstName,
                LastName = user.LastName,
                CentreId = user.CentreId,
                Token = "",
            };
            return userOutDto;
        }

        public bool EditExistingUserAccessLevel(UserAccessInDto userAccessInDto)
        {
            User user = _dbContext.Users.FirstOrDefault(e => e.Id == userAccessInDto.UserId);
            if (user == null)
            {
                return false;
            }
            else
            {
                user.UserType = userAccessInDto.UserType;
                EntityEntry<User> e = _dbContext.Users.Update(user);
                User userEntity = e.Entity;
                _dbContext.SaveChanges();

                return true;
            }
        }

        // Dog Methods
        public bool AddNewDog(DogInDTO dogInfo)
        {
            Dog dog = new Dog
            {
                Name = dogInfo.Name,
                Breed = dogInfo.Breed,
                CentreId = dogInfo.CentreId,
                isFlag = false,
                isAlert = false,
            };
            _dbContext.Add(dog);
            _dbContext.SaveChanges();
            return true;
        }

        public bool DeleteDog(int dogId)
        {
            Dog dog = _dbContext.Dogs.FirstOrDefault(e => e.Id == dogId);
            if (dog == null)
            {
                return false;
            }
            else
            {
                _dbContext.Dogs.Remove(dog);
                _dbContext.SaveChanges();
                return true;
            }
        }

        public int getUserIdFromUserName(string username)
        {
            User user = _dbContext.Users.FirstOrDefault(e => e.UserName == username);
            if (user == null)
            {
                return -1;
            }
            else
            {
                return user.Id;
            }
        }

        public bool ChangePasswordForCurrentUser(int UserId, UserChangePasswordInDto userChangePasswordInDto)
        {
            User user = _dbContext.Users.FirstOrDefault(e => e.Id == UserId);

            if (user == null)
            {
                return false;
            }
            else
            {
                if(user.PasswordSha256Hash != SPCAAuthHandler.getSha256Hash(userChangePasswordInDto.OldPassword))
                {
                    return false;
                } 
                else
                {
                    user.PasswordSha256Hash = SPCAAuthHandler.getSha256Hash(userChangePasswordInDto.NewPassword);

                    EntityEntry<User> e = _dbContext.Users.Update(user);
                    User userEntity = e.Entity;
                    _dbContext.SaveChanges();

                    return true;
                }
            }
        }

        public IEnumerable<DogOutDTO> ListAllDogsAllCentres()
        {
            List<DogOutDTO> AllDogs = new List<DogOutDTO>();
            IEnumerable<Dog> dogs = _dbContext.Dogs.ToList();
            foreach (Dog dog in dogs) { AllDogs.Add(ConvertToDogOutDTO(dog)); }
            return AllDogs;
        }

        public IEnumerable<DogOutDTO> ListAllDogsInACentre(int centreId)
        {
            List<DogOutDTO> AllDogs = new List<DogOutDTO>();
            IEnumerable<Dog> dogs = _dbContext.Dogs.Where(e => e.CentreId == centreId);
            foreach (Dog dog in dogs) { AllDogs.Add(ConvertToDogOutDTO(dog)); }
            return AllDogs;
        }

        public DogOutDTO GetDogInformationAllCentres(int dogId)
        {
            Dog dog = _dbContext.Dogs.FirstOrDefault(e => e.Id == dogId);
            if (dog == null)
            {
                return new DogOutDTO
                {
                    Id = -1,
                    Name = "",
                    Breed = "",
                    CentreId = 0,
                    isFlag = false,
                    isAlert = false,
                };

            }
            return ConvertToDogOutDTO(dog);
        }

        public DogOutDTO GetDogInformationOwnCentre(int dogId, int centreId)
        {
            Dog dog = _dbContext.Dogs.FirstOrDefault(e => e.Id == dogId && e.CentreId == centreId);
            if (dog == null)
            {
                return new DogOutDTO
                {
                    Id = -1,
                    Name = "",
                    Breed = "",
                    CentreId = 0,
                    isFlag = false,
                    isAlert = false,
                };

            }
            return ConvertToDogOutDTO(dog);
        }

        public DogOutDTO ConvertToDogOutDTO(Dog dog)
        {
            DogOutDTO dogOutDto = new DogOutDTO
            {
                Id = dog.Id,
                Name = dog.Name,
                Breed = dog.Breed,
                CentreId = dog.CentreId,
                isFlag = dog.isFlag,
                isAlert = dog.isAlert,
            };
            return dogOutDto;
        }

        public bool EditDogInformation(DogEditInDto dog)
        {
            Dog dogToBeChanged = _dbContext.Dogs.FirstOrDefault(e => e.Id == dog.Id);

            if (dogToBeChanged == null)
            {
                return false;
            }
            else
            {
                dogToBeChanged.CentreId = dog.CentreId;
                dogToBeChanged.Breed = dog.Breed;
                dogToBeChanged.Name = dog.Name;

                EntityEntry<Dog> e = _dbContext.Dogs.Update(dogToBeChanged);
                Dog dogEntity = e.Entity;
                _dbContext.SaveChanges();

                return true;
            }   
        }

        public bool toggleDogFlag(int dogId)
        {
            Dog dog = _dbContext.Dogs.FirstOrDefault(e => e.Id == dogId);

            if (dog == null)
            {
                return false;
            }
            else
            {
                dog.isFlag = !dog.isFlag;
                EntityEntry<Dog> e = _dbContext.Dogs.Update(dog);
                Dog dogEntity = e.Entity;
                _dbContext.SaveChanges();

                return true;

            }
        }

        public bool toggleDogAlert(int dogId)
        {
            Dog dog = _dbContext.Dogs.FirstOrDefault(e => e.Id == dogId);

            if (dog == null)
            {
                return false;
            }
            else
            {
                dog.isAlert = !dog.isAlert;
                EntityEntry<Dog> e = _dbContext.Dogs.Update(dog);
                Dog dogEntity = e.Entity;
                _dbContext.SaveChanges();

                return true;

            }
        }

        public bool addNewRequest(RequestInDto requestInDto)
        {
            addNewRequestHelper(requestInDto);
            return true;
        }

        public DogWeightRequestOutDto getCurrentDogRequestWeight(int dogId)
        {
            Request requestCheck = _dbContext.Requests.FirstOrDefault(e => e.DogId == dogId);

            if (requestCheck == null)
            {
                return null;

            } else
            {
                DogWeightRequestOutDto currentDogRequestWeight = new DogWeightRequestOutDto
                {
                    DogId = dogId,
                    Weight = requestCheck.DogWeight,
                };

                return currentDogRequestWeight;
            }

        }

        private Request addNewRequestHelper(RequestInDto requestInDto)
        {
            // remove expired requests-----------------------
            int currentTimeStampInSecond = (int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds;

            IEnumerable<Request> listOfRequests = _dbContext.Requests.ToList();

            foreach (Request request in listOfRequests)
            {
                if (int.Parse(request.TimeStamp) + REQUEST_EXPIRING_MINUTES * 60 < currentTimeStampInSecond)
                {
                    _dbContext.Remove(request);
                    _dbContext.SaveChanges();
                }
            }
            //-----------------------------------------------


            Request requestCheck = _dbContext.Requests.FirstOrDefault(e => e.ScaleId == requestInDto.ScaleId && e.DogId == requestInDto.DogId);

            if (requestCheck == null)
            {
                Request newRequest = new Request
                {
                    ScaleId = requestInDto.ScaleId,
                    DogId = requestInDto.DogId,
                    DogWeight = 0.0,
                    TimeStamp = Convert.ToString((int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds),
            };

                EntityEntry<Request> e = _dbContext.Requests.Add(newRequest);
                _dbContext.SaveChanges();
                return newRequest;
            }
            else
            {
                requestCheck.DogWeight = 0.0;
                requestCheck.TimeStamp = Convert.ToString((int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds);

                EntityEntry<Request> e = _dbContext.Requests.Update(requestCheck);
                Request requestEntity = e.Entity;
                _dbContext.SaveChanges();

                return requestCheck;
            }
        }

        public bool addWeightFromScaleToRequest(ScaleWeightRequestInDto scaleWeightRequestInDto)
        {
            Request requestCheck = _dbContext.Requests.FirstOrDefault(e => e.ScaleId == scaleWeightRequestInDto.ScaleId);

            if (requestCheck == null)
            {
                return false;
            }
            else
            {
                requestCheck.DogWeight = scaleWeightRequestInDto.Weight;

                EntityEntry<Request> e = _dbContext.Requests.Update(requestCheck);
                Request requestEntity = e.Entity;
                _dbContext.SaveChanges();

                return true;
            }
            
        }

        // Util Methods
        public bool AddNewScale(ScaleInDTO scaleDTO)
        {
            Scale existing = _dbContext.Scales.FirstOrDefault(s => s.Name == scaleDTO.Name && s.CentreId == scaleDTO.CentreId);
            if (existing == null)
            {
                Scale scale = new Scale
                {
                    Name = scaleDTO.Name,
                    CentreId = scaleDTO.CentreId,
                };
                _dbContext.Add(scale);
                _dbContext.SaveChanges();
                return true;
            }
            else
            {
                return false;
            }
            
        }

        public bool DeleteScale(int scaleId)
        {
            Scale scale = _dbContext.Scales.FirstOrDefault(e => e.Id == scaleId);
            if (scale == null)
            {
                return false;
            }
            else
            {
                _dbContext.Remove(scale);
                _dbContext.SaveChanges();
                return true;
            }
        }

        public IEnumerable<Scale> ListAllScales() {
            IEnumerable<Scale> listOfScales = _dbContext.Scales.ToList();
            return listOfScales;
        }

        public bool AddNewCentre(string name)
        {
            Centre existingCentre = _dbContext.Centres.FirstOrDefault(e => e.Name == name);
            if (existingCentre == null)
            {
                Centre centre = new Centre { Name = name };
                _dbContext.Add(centre);
                _dbContext.SaveChanges();
                return true;
            }
            else{
                return false;
            }
            
        }

        public bool DeleteCentre(int centreId)
        {
            Centre existingCentre = _dbContext.Centres.FirstOrDefault(e => e.Id == centreId);
            if (existingCentre == null)
            {
                return false;
            }
            else
            {
                _dbContext.Remove(existingCentre);
                _dbContext.SaveChanges();
                return true;
            }

        }


        public IEnumerable<Centre> ListAllCentres() {
            IEnumerable<Centre> listOfCentres = _dbContext.Centres.ToList();
            return listOfCentres;
        }

    }
}
