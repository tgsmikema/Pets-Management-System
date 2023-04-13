﻿using Microsoft.EntityFrameworkCore;
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

            UserOutDto userOutDto = new UserOutDto
            {
                UserName = username,
                UserType = user.UserType,
                FirstName = user.FirstName,
                LastName = user.LastName,
                CentreId = user.CentreId,
                Token = "",
            };

            return userOutDto;
        }
    }
}
