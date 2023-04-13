using Microsoft.EntityFrameworkCore;
using SPCA_backend.Model;

namespace SPCA_backend.Data
{
    public class SPCA_DBContext : DbContext
    {
        public SPCA_DBContext(DbContextOptions<SPCA_DBContext> options) : base(options) { }
        public DbSet<User> Users { get; set; }
        public DbSet<Centre> Centres { get; set; }
        public DbSet<Dog> Dogs { get; set; }
        public DbSet<Message> Messages { get; set; }
        public DbSet<Request> Requests { get; set; }
        public DbSet<Scale> Scales { get; set; }
        public DbSet<Weight> Weights { get; set; }

    }
}
