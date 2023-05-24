using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SPCA_backend.Dtos;
using SPCA_backend.Model;
using SPCA_backend.Handler;
using Azure.Core;
using Request = SPCA_backend.Model.Request;
using MailKit.Net.Smtp;
using MailKit.Security;
using Microsoft.Extensions.Options;
using MimeKit;
using MimeKit.Text;


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



        // ------------------------------------------------------------------User------------------------------------------------------------------
        public bool ValidLoginAdmin(string username, string passwordSha256Hash)
        {
            User userLogin = _dbContext.Users.FirstOrDefault
               (e => e.UserName == username && e.PasswordSha256Hash == passwordSha256Hash && e.UserType == "admin");
            return userLogin != null;
        }

        public bool ValidLoginVets(string username, string passwordSha256Hash)
        {
            User userLogin = _dbContext.Users.FirstOrDefault
               (e => e.UserName == username && e.PasswordSha256Hash == passwordSha256Hash && e.UserType == "vet");
            return userLogin != null;
        }

        public bool ValidLoginVolunteers(string username, string passwordSha256Hash)
        {
            User userLogin = _dbContext.Users.FirstOrDefault
               (e => e.UserName == username && e.PasswordSha256Hash == passwordSha256Hash && e.UserType == "volunteer");
            return userLogin != null;
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
                    Email = userInDto.Email,
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

        public int GetUserIdFromUserName(string username)
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
                if (user.PasswordSha256Hash != SPCAAuthHandler.getSha256Hash(userChangePasswordInDto.OldPassword))
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


        // ------------------------------------------------------------------Dog------------------------------------------------------------------
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
                    LastCheckInTimeStamp = "0",
                    LastCheckInWeight = 0,
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
                    LastCheckInTimeStamp = "0",
                    LastCheckInWeight = 0,
                };

            }
            return ConvertToDogOutDTO(dog);
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

        public bool ToggleDogFlag(int dogId)
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

        public bool ToggleDogAlert(int dogId)
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

        public bool AddNewRequest(RequestInDto requestInDto)
        {
            addNewRequestHelper(requestInDto);
            return true;
        }

        public bool AddWeightFromScaleToRequest(ScaleWeightRequestInDto scaleWeightRequestInDto)
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

        public DogWeightRequestOutDto GetCurrentDogRequestWeight(int dogId)
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
 

        public bool SaveCurrentWeight(int dogId)
        {
            Request requestCheck = _dbContext.Requests.FirstOrDefault(e => e.DogId == dogId);

            if (requestCheck == null || requestCheck.DogWeight == 0)
            {
                return false;
            }
            else
            {
                Weight weight = new Weight
                {
                    DogId = dogId,
                    DogWeight = requestCheck.DogWeight,
                    TimeStamp = Convert.ToString((int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds),
                };

                _dbContext.Remove(requestCheck);
                _dbContext.Add(weight);
                _dbContext.SaveChanges();

                return true;

            }

        }

        public IEnumerable<Weight> GetWeightHistoryForADog(int dogId)
        {
            IEnumerable<Weight> weightHistory = _dbContext.Weights.Where(e => e.DogId == dogId).OrderByDescending(e => e.TimeStamp);

            return weightHistory;
        }

        // -------------------------------------------------------------------------Util-----------------------------------------------------------------------
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

        public StatsOutDTO getCurrentWeekStats(int centerId)
        {
            string startOfTheWeekMondayTimeStamp = convertDateTimeToTimestamp(DateTime.Today.AddDays(-(int)DateTime.Today.DayOfWeek + (int)DayOfWeek.Monday));
            int startOfWeek = int.Parse(startOfTheWeekMondayTimeStamp);
            int rightNow = (int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds;

            StatsOutDTO statsOutDTO = getStatsFromTimestampRangeAndCenterId(startOfWeek, rightNow, centerId);
            return statsOutDTO;

        }


        public IEnumerable<StatsOutDTO> getWeeklyStats(StatsInDTO statsInDTO)
        {
            int minTimestamp = int.Parse(statsInDTO.minTimestamp);
            int maxTimestamp = int.Parse(statsInDTO.maxTimestamp);
            int centerId = statsInDTO.centerId;

            List<StatsOutDTO> list = new List<StatsOutDTO>();

            for (int i = minTimestamp; i < maxTimestamp; i+= 86400)
            {
                list.Add(getStatsFromTimestampRangeAndCenterId(i, i + 86400, centerId));
            }

            return list.AsEnumerable();

        }


        public IEnumerable<StatsOutDTO> getMonthlyStats(StatsInDTO statsInDTO)
        {
            int minTimestamp = int.Parse(statsInDTO.minTimestamp);
            int maxTimestamp = int.Parse(statsInDTO.maxTimestamp);
            int centerId = statsInDTO.centerId;

            List<StatsOutDTO> list = new List<StatsOutDTO>();

            for (int i = minTimestamp; i < maxTimestamp; i += 345600) // a point every 4 days
            {
                list.Add(getStatsFromTimestampRangeAndCenterId(i, i + 345600, centerId));
            }

            return list.AsEnumerable();
        }

        // ------------------------------------------------------------------Message------------------------------------------------------------------


        public async Task AddNewMessage(MessageInDto messageInDto)
        {
            Message newMessage = new Message
            {
                FromUserId = messageInDto.FromUserId,
                ToUserId = messageInDto.ToUserId,
                IsRead = true,
                MessageContent = messageInDto.MessageContent,
                TimeStamp = Convert.ToString((int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds),
            };

            EntityEntry<Message> e = _dbContext.Messages.Add(newMessage);
            _dbContext.SaveChanges();

            await SendEmail(messageInDto.FromUserId, messageInDto.ToUserId, messageInDto.MessageContent);

        }


        public IEnumerable<UserOutDto> getAlreadyMessagedPeopleList(int currentUserId)
        {
            HashSet<int> peopleId = new HashSet<int>();
            IEnumerable<Message> allRelatedMessages = _dbContext.Messages.Where(e => (e.ToUserId == currentUserId || e.FromUserId == currentUserId));

            foreach (Message m in allRelatedMessages)
            {
                peopleId.Add(m.FromUserId);
                peopleId.Add(m.ToUserId);
            }

            peopleId.Remove(currentUserId);

            IEnumerable<User> allUsers = _dbContext.Users.ToList();
            List<UserOutDto> usersOut = new List<UserOutDto>();

            foreach(User u in allUsers)
            {
                if (peopleId.Contains(u.Id))
                {
                    UserOutDto uOut = new UserOutDto
                    {
                        Id = u.Id,
                        UserName = u.UserName,
                        UserType = u.UserType,
                        FirstName = u.FirstName,
                        LastName = u.LastName,
                        Email = u.Email,
                        CentreId = u.CentreId,
                        Token = "hidden"
                    };
                    usersOut.Add(uOut);
                }
            }

            return usersOut.AsEnumerable();
        }

        public IEnumerable<UserOutDto> getNeverMessagedPeopleList(int currentUserId)
        {
            HashSet<int> peopleId = new HashSet<int>();
            IEnumerable<Message> allRelatedMessages = _dbContext.Messages.Where(e => (e.ToUserId == currentUserId || e.FromUserId == currentUserId));

            foreach (Message m in allRelatedMessages)
            {
                peopleId.Add(m.FromUserId);
                peopleId.Add(m.ToUserId);
            }

            peopleId.Remove(currentUserId);

            IEnumerable<User> allUsers = _dbContext.Users.Where(u => u.Id != currentUserId);
            List<UserOutDto> usersOut = new List<UserOutDto>();

            foreach (User u in allUsers)
            {
                if (!peopleId.Contains(u.Id))
                {
                    UserOutDto uOut = new UserOutDto
                    {
                        Id = u.Id,
                        UserName = u.UserName,
                        UserType = u.UserType,
                        FirstName = u.FirstName,
                        LastName = u.LastName,
                        Email = u.Email,
                        CentreId = u.CentreId,
                        Token = "hidden"
                    };
                    usersOut.Add(uOut);
                }
            }

            return usersOut.AsEnumerable();
        }

        public IEnumerable<MessageOutDto> getChatHistory(int currentUserId, int chatToUserId)
        {
            List<MessageOutDto> messageOutDtos = new List<MessageOutDto>();
            
            IEnumerable<Message> allRelatedMessages = _dbContext.Messages
                .Where(e => ((e.ToUserId == currentUserId && e.FromUserId == chatToUserId) || (e.ToUserId == chatToUserId && e.FromUserId == currentUserId)))
                .OrderBy(e => e.TimeStamp);

            foreach(Message m in allRelatedMessages)
            {
                MessageOutDto mOut = new MessageOutDto
                {
                    FromUserId = m.FromUserId,
                    ToUserId = m.ToUserId,
                    Timestamp = m.TimeStamp,
                    MessageContent = m.MessageContent
                };
                messageOutDtos.Add(mOut);
            }

            return messageOutDtos.AsEnumerable();
        }


        //---------------------------------------------------------------------Helper Methods----------------------------------------------------------------

        private StatsOutDTO getStatsFromTimestampRangeAndCenterId (int minTimestamp, int maxTimestamp, int centerId)
        {
            List<Dog> selectedDogs = new List<Dog>();
            List<Weight> weights = new List<Weight>();

            if (centerId == 0)
            {
                selectedDogs = _dbContext.Dogs.ToList().ToList();
                // return weights from all centers.
                IEnumerable<Weight> allWeightsTemp = _dbContext.Weights.ToList(); //.Where(e => int.Parse(e.TimeStamp) >= startOfWeek);
                foreach (Weight w in allWeightsTemp)
                {
                    if (int.Parse(w.TimeStamp) >= minTimestamp && int.Parse(w.TimeStamp) <= maxTimestamp)
                    {
                        weights.Add(w);
                    }
                }
            }
            else
            {
                IEnumerable<Dog> allDogsFromCurrentCenter = _dbContext.Dogs.Where(e => e.CentreId == centerId);
                selectedDogs = allDogsFromCurrentCenter.ToList();
                List<int> allDogIdFromCurrentCenter = new List<int>();
                foreach (Dog dog in allDogsFromCurrentCenter)
                {
                    allDogIdFromCurrentCenter.Add(dog.Id);
                }
                IEnumerable<Weight> allWeightsTemp = _dbContext.Weights.ToList();
                List<Weight> weightsTemp = new List<Weight>();
                foreach (Weight w in allWeightsTemp)
                {
                    if (int.Parse(w.TimeStamp) >= minTimestamp && int.Parse(w.TimeStamp) <= maxTimestamp)
                    {
                        weightsTemp.Add(w);
                    }
                }
                weights = weightsTemp.Where(e => allDogIdFromCurrentCenter.Contains(e.DogId)).ToList();
            }

            HashSet<int> dogIdWeighted = new HashSet<int>();

            foreach (Weight w in weights)
            {
                dogIdWeighted.Add(w.DogId);
            }

            int weighted = dogIdWeighted.Count();
            int unweighted = selectedDogs.Count() - weighted;

            StatsOutDTO statsOutDTO = new StatsOutDTO
            {
                TimeStamp = Convert.ToString(minTimestamp),
                NoOfDogsWeighted = weighted,
                NoOfDogsUnweighted = unweighted
            };

            return statsOutDTO;
        }

        private string convertDateTimeToTimestamp(DateTime datetime)
        {
            return Convert.ToString((int)datetime.Subtract(new DateTime(1970, 1, 1)).TotalSeconds);
        }

        private async Task SendEmail(int fromUserId, int toUserId, string messageContent)
        {
            User userFrom = _dbContext.Users.FirstOrDefault(e => e.Id == fromUserId);
            User userTo = _dbContext.Users.FirstOrDefault(e => e.Id == toUserId);

            var email = new MimeMessage();
            email.From.Add(MailboxAddress.Parse("capstone770team2admin@zohomail.com.au"));
            email.To.Add(MailboxAddress.Parse(userTo.Email));
            email.Subject = "[SPCA] " + userFrom.FirstName + " " + userFrom.LastName + 
                " have sent you a message!";
            string bodyContent = "Hi, " + userTo.FirstName + " \n\n"
                + "You have just received a message from " + userFrom.FirstName + " " + userFrom.LastName + ",\n" +
                "The message is quoted below, you can also view it from the chat history in the app: \n\n" + 
                "----------------------------------------------------------------------------------------------------\n\n" +
                messageContent;
            email.Body = new TextPart(TextFormat.Plain) { Text = bodyContent };

            // send email
            using var smtp = new SmtpClient();
            smtp.Connect("smtp.zoho.com.au", 587, SecureSocketOptions.StartTls);
            smtp.Authenticate("tgsmikema@gmail.com", "2fast2furious!");
            smtp.Send(email);
            smtp.Disconnect(true);

        }



        private UserOutDto ConvertToUserOutDTO(User user)
        {
            UserOutDto userOutDto = new UserOutDto
            {
                Id = user.Id,
                UserName = user.UserName,
                Email = user.Email,
                UserType = user.UserType,
                FirstName = user.FirstName,
                LastName = user.LastName,
                CentreId = user.CentreId,
                Token = "",
            };
            return userOutDto;
        }

        private DogOutDTO ConvertToDogOutDTO(Dog dog)
        {
            Weight dogWeight = _dbContext.Weights.Where(e => e.DogId == dog.Id).OrderByDescending(e => e.TimeStamp).FirstOrDefault();
            if (dogWeight == null)
            {
                dogWeight = new Weight
                {
                    TimeStamp = "0",
                    DogWeight = 0,
                };
            }

            DogOutDTO dogOutDto = new DogOutDTO
            {
                Id = dog.Id,
                Name = dog.Name,
                Breed = dog.Breed,
                CentreId = dog.CentreId,
                isFlag = dog.isFlag,
                isAlert = dog.isAlert,
                LastCheckInTimeStamp = dogWeight.TimeStamp,
                LastCheckInWeight = dogWeight.DogWeight,
            };
            return dogOutDto;
        }

        private Request addNewRequestHelper(RequestInDto requestInDto)
        {
            
            int currentTimeStampInSecond = (int)DateTime.Now.Subtract(new DateTime(1970, 1, 1)).TotalSeconds;

            // remove same dog id on all other scales
            IEnumerable<Request> listOfRequestsHasSameDogOnOtherScales = _dbContext.Requests.ToList().Where(e => (e.ScaleId != requestInDto.ScaleId) && (e.DogId == requestInDto.DogId));

            foreach (Request req in listOfRequestsHasSameDogOnOtherScales)
            {
                    _dbContext.Remove(req);
                    _dbContext.SaveChanges();
            }

            // remove expired requests--- (only delete the current scale stale requests)
            IEnumerable<Request> listOfRequests = _dbContext.Requests.ToList().Where(e => e.ScaleId == requestInDto.ScaleId);

            foreach (Request request in listOfRequests)
            {
                if (int.Parse(request.TimeStamp) + 2 < currentTimeStampInSecond)
                {
                    _dbContext.Remove(request);
                    _dbContext.SaveChanges();
                }
            }
            //-----------------------------


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

    }
}
