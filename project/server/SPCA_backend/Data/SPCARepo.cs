using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SPCA_backend.Dtos;
using SPCA_backend.Model;
using SPCA_backend.Handler;

namespace SPCA_backend.Data
{
    public class SPCARepo : ISPCARepo
    {
        private readonly SPCA_DBContext _dbContext;
        public SPCARepo(SPCA_DBContext dbContext)
        {
            _dbContext = dbContext;
        }

        public bool ValidLoginAdmin(string username, string passwordSha256Hash)
        {
            UserLogin userLogin = _dbContext.UserLogins.FirstOrDefault
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
            UserLogin userLogin = _dbContext.UserLogins.FirstOrDefault
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
            UserLogin userLogin = _dbContext.UserLogins.FirstOrDefault
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

        public bool AddNewUser(UserLoginInDto userLoginInDto)
        {
            UserLogin userCheck = _dbContext.UserLogins.FirstOrDefault(e => e.UserName == userLoginInDto.UserName);

            if (userCheck == null)
            {
                UserLogin newUser = new UserLogin
                {
                    UserName = userLoginInDto.UserName,
                    PasswordSha256Hash = SPCAAuthHandler.getSha256Hash(userLoginInDto.Password),
                    UserType = userLoginInDto.UserType
                };

                EntityEntry<UserLogin> e = _dbContext.UserLogins.Add(newUser);
                _dbContext.SaveChanges();
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
