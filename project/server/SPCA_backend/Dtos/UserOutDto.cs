namespace SPCA_backend.Dtos
{
    public class UserOutDto
    {
        public int Id {  get; set; }
        public string UserName { get; set; }
        public string UserType { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public int CentreId { get; set; }
        public string Token { get; set; }
    }
}
