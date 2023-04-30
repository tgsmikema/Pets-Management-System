using Microsoft.Identity.Client;

namespace SPCA_backend.Dtos
{
    public class StatsInDTO
    {
        public int centerId { get; set; }
        public string minTimestamp { get; set; }
        public string maxTimestamp { get; set; }
    }
}
