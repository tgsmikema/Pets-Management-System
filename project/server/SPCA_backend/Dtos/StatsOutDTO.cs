using Microsoft.Identity.Client;

namespace SPCA_backend.Dtos
{
    public class StatsOutDTO
    {
        public string TimeStamp { get; set; }
        public int NoOfDogsWeighted { get; set; }
        public int NoOfDogsUnweighted { get; set; }
    }
}
