#!/usr/bin/env python3
"""
Listening Generator for B2 (8 new scenarios, bringing B2 total to 10).
All scenarios adhere to listening.schema.json:
- CEFR: B2
- Valid category enum
- Real speakers
- Timestamped transcript items with text_en and text_tr
- 5 MCQs per scenario using McqBalancer
- Key vocabulary and related_ids with verified IDs
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=603)

def build_q(qid, q_en, q_tr_hint, correct, distractors, exp_en, exp_tr):
    options, idx = balancer.balance_options(correct, distractors)
    return {
        "id": qid,
        "question_en": q_en,
        "question_tr_hint": q_tr_hint,
        "options": options,
        "correct_answer": correct,
        "explanation_en": exp_en,
        "explanation_tr": exp_tr
    }

B2_LISTENING_SCENARIOS = [
    {
        "id": "listening.b2.database-migration-failover",
        "title": "Incident Response: Zero-Downtime Database Failover",
        "cefr_level": "B2",
        "category": "incident_response",
        "scenario_context": "Murat, a site reliability engineer, coordinates with Rebecca, the lead DBA, as the primary PostgreSQL node experiences hardware degradation.",
        "speakers": [
            {"id": "murat", "name": "Murat", "role": "Site Reliability Engineer", "accent": "Turkish"},
            {"id": "rebecca", "name": "Rebecca", "role": "Lead Database Administrator", "accent": "British"}
        ],
        "audio_ref": "audio/listening/b2_database_failover.mp3",
        "duration_seconds": 54,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "murat",
                "start_ms": 0,
                "end_ms": 6800,
                "text_en": "Rebecca, our monitoring alerts just triggered on database cluster zero-one. Disk I/O latency has spiked above four hundred milliseconds.",
                "text_tr": "Rebecca, sıfır bir nolu veritabanı kümesinde izleme uyarılarımız az önce tetiklendi. Disk G/Ç gecikmesi dört yüz milisaniyenin üzerine fırladı."
            },
            {
                "index": 2,
                "speaker_id": "rebecca",
                "start_ms": 7200,
                "end_ms": 15800,
                "text_en": "I see the hardware degradation logs. Storage controllers are reporting corrupted sectors. We need to initiate an immediate failover to the standby replica.",
                "text_tr": "Donanım bozulma günlüklerini görüyorum. Depolama kontrolörleri bozuk sektörler bildiriyor. Bekleme modundaki kopyaya (standby replica) derhal yük devretme (failover) başlatmamız gerekiyor."
            },
            {
                "index": 3,
                "speaker_id": "murat",
                "start_ms": 16200,
                "end_ms": 23500,
                "text_en": "What is the current replication lag on the read-replica in availability zone B? Can we guarantee zero data loss?",
                "text_tr": "B kullanılabilirlik bölgesindeki salt-okunur kopyada mevcut çoğaltma gecikmesi nedir? Sıfır veri kaybını garanti edebilir miyiz?"
            },
            {
                "index": 4,
                "speaker_id": "rebecca",
                "start_ms": 23900,
                "end_ms": 33200,
                "text_en": "The replication lag is under forty milliseconds. Because we operate in synchronous replication mode, transactions are already committed to the WAL logs on both nodes.",
                "text_tr": "Çoğaltma gecikmesi kırk milisaniyenin altında. Eşzamanlı çoğaltma modunda çalıştığımız için işlemler her iki düğümdeki WAL günlüklerine çoktan işlendi."
            },
            {
                "index": 5,
                "speaker_id": "murat",
                "start_ms": 33600,
                "end_ms": 42800,
                "text_en": "Understood. I will reroute connection pooling traffic through PgBouncer, promote the replica to primary, and monitor application error rates.",
                "text_tr": "Anlaşıldı. Bağlantı havuzu trafiğini PgBouncer üzerinden yeniden yönlendirecek, kopyayı birincil düğüme yükseltecek ve uygulama hata oranlarını izleyeceğim."
            },
            {
                "index": 6,
                "speaker_id": "rebecca",
                "start_ms": 43200,
                "end_ms": 52500,
                "text_en": "Promoting standby node now. Traffic transitioned smoothly with only three transient timeout errors. Latency is back down to two milliseconds.",
                "text_tr": "Bekleme düğümünü şimdi yükseltiyorum. Trafik yalnızca üç geçici zaman aşımı hatasıyla sorunsuzca aktarıldı. Gecikme tekrar iki milisaniyeye düştü."
            }
        ],
        "key_vocabulary": [
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "Disk I/O ve veritabanı yanıtlarında meydana gelen işlem gecikmesi."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Donanım arızalarında sıfır kesintiyle toparlanma sistem direnci."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_fail_01",
                "What critical symptom first alerted Murat to the database anomaly?",
                "Murat'ı veritabanı anomalisine karşı ilk uyaran kritik belirti neydi?",
                "Disk I/O latency surged above four hundred milliseconds on cluster zero-one",
                [
                    "The physical server chassis caught fire in the local server room",
                    "A hacker transferred all customer funds to an offshore bank account",
                    "All database passwords were deleted by a rogue terminal command"
                ],
                "Murat opens by reporting that disk I/O latency spiked above 400 milliseconds on database cluster 01.",
                "Murat 01 nolu kümede disk G/Ç gecikmesinin 400 milisaniyenin üzerine çıktığını bildirir."
            ),
            build_q(
                "q_b2_fail_02",
                "What was the root cause detected by the hardware controllers?",
                "Donanım kontrolörleri tarafından tespit edilen kök neden neydi?",
                "Corrupted storage sectors caused by storage controller degradation",
                [
                    "A power failure across the entire European electrical grid",
                    "A sudden rise in room temperature due to broken air conditioning",
                    "A junior developer accidentally dropping a glass of water on the hard drive"
                ],
                "Rebecca states: 'Storage controllers are reporting corrupted sectors.'",
                "Rebecca depolama kontrolörlerinin bozuk sektörler bildirdiğini ifade eder."
            ),
            build_q(
                "q_b2_fail_03",
                "Why were Rebecca and Murat confident that zero data loss would occur during failover?",
                "Rebecca ve Murat yük devretme sırasında neden sıfır veri kaybı yaşanacağından emindiler?",
                "They operate in synchronous replication mode with write-ahead logs committed to both nodes",
                [
                    "They had printed every database record onto physical paper sheets",
                    "They were using an external AI tool that memorizes all customer data",
                    "The database was completely empty with zero real customer users"
                ],
                "Rebecca explains synchronous replication committed transactions to WAL logs on both nodes with under 40ms lag.",
                "Rebecca eşzamanlı çoğaltma modu sayesinde işlemlerin her iki düğümdeki günlüklere işlendiğini belirtir."
            ),
            build_q(
                "q_b2_fail_04",
                "Which proxy tool does Murat use to manage connection pooling during the transition?",
                "Murat geçiş sırasında bağlantı havuzunu yönetmek için hangi aracı kullanır?",
                "PgBouncer",
                [
                    "Apache HTTP Server",
                    "Docker Desktop",
                    "Microsoft Word"
                ],
                "Murat confirms: 'I will reroute connection pooling traffic through PgBouncer.'",
                "Murat trafiği PgBouncer üzerinden yönlendireceğini açıklar."
            ),
            build_q(
                "q_b2_fail_05",
                "What was the final outcome after promoting the standby replica?",
                "Bekleme kopyası yükseltildikten sonra nihai sonuç ne oldu?",
                "Traffic transitioned smoothly, and latency dropped back to two milliseconds",
                [
                    "The entire application crashed and remained offline for forty-eight hours",
                    "All existing customer accounts had to be manually re-registered",
                    "The database cluster had to be abandoned and reconstructed from scratch"
                ],
                "Rebecca reports the transition was smooth with only 3 transient errors, and latency returned to 2ms.",
                "Rebecca geçişin sorunsuz tamamlandığını ve gecikmenin 2 milisaniyeye düştüğünü bildirir."
            )
        ],
        "topic_tags": ["database", "reliability", "failover", "postgresql", "high-availability"],
        "related_ids": ["vocab.latency", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.cloud-cost-finops-review",
        "title": "FinOps Audit: Reining in Over-Provisioned Cloud Compute",
        "cefr_level": "B2",
        "category": "executive_briefing",
        "scenario_context": "Sinan, a cloud solutions architect, and Catherine, the FinOps lead, review monthly cloud expenditures to eliminate waste and optimize reserved instances.",
        "speakers": [
            {"id": "sinan", "name": "Sinan", "role": "Cloud Solutions Architect", "accent": "Turkish"},
            {"id": "catherine", "name": "Catherine", "role": "FinOps Lead", "accent": "American"}
        ],
        "audio_ref": "audio/listening/b2_finops_audit.mp3",
        "duration_seconds": 53,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "catherine",
                "start_ms": 0,
                "end_ms": 6500,
                "text_en": "Sinan, our cloud infrastructure bill increased by twenty-eight percent last quarter, exceeding our budget by sixty thousand dollars.",
                "text_tr": "Sinan, bulut altyapı faturamız geçen çeyrekte yüzde yirmi sekiz artarak bütçemizi altmış bin dolar aştı."
            },
            {
                "index": 2,
                "speaker_id": "sinan",
                "start_ms": 6900,
                "end_ms": 15400,
                "text_en": "I performed a cost allocation breakdown. The primary driver is our staging and load-testing clusters running on twenty-four seven on-demand instances.",
                "text_tr": "Bir maliyet dağılımı analizi yaptım. Ana etken, talep üzerine (on-demand) örneklerde yedi yirmi dört çalışan staging ve yük testi kümelerimiz."
            },
            {
                "index": 3,
                "speaker_id": "catherine",
                "start_ms": 15800,
                "end_ms": 23500,
                "text_en": "That is completely unnecessary. Why are staging environments consuming expensive compute over weekends and overnight hours?",
                "text_tr": "Bu tamamen gereksiz. Staging ortamları neden hafta sonları ve gece saatlerinde pahalı bilişim kaynağı tüketiyor?"
            },
            {
                "index": 4,
                "speaker_id": "sinan",
                "start_ms": 23900,
                "end_ms": 32800,
                "text_en": "Development teams forgot to shut down their test nodes. We can deploy an automated cron schedule to scale staging replicas down to zero after seven p.m.",
                "text_tr": "Geliştirme ekipleri test düğümlerini kapatmayı unuttu. Akşam saat yediden sonra staging kopyalarını sıfıra indirecek otomatik bir zamanlayıcı devreye sokabiliriz."
            },
            {
                "index": 5,
                "speaker_id": "catherine",
                "start_ms": 33200,
                "end_ms": 42000,
                "text_en": "That alone will save thirty thousand monthly. What about our production baseline workloads? Can we purchase three-year compute savings plans?",
                "text_tr": "Yalnızca bu ayda otuz bin tasarruf sağlayacaktır. Peki ya üretim taban iş yüklerimiz? Üç yıllık bilişim tasarruf planları satın alabilir miyiz?"
            },
            {
                "index": 6,
                "speaker_id": "sinan",
                "start_ms": 42400,
                "end_ms": 51500,
                "text_en": "Yes. Our steady-state baseline is sixty instances. Committing to a three-year savings plan will yield an additional thirty-five percent discount.",
                "text_tr": "Evet. Sabit taban seviyemiz altmış örnek. Üç yıllık bir tasarruf planına taahhüt vermek ek yüzde otuz beş indirim sağlayacaktır."
            }
        ],
        "key_vocabulary": [
            {
                "word": "audit",
                "vocab_id": "vocab.audit",
                "context_note_tr": "Bulut kaynakları ve finansal harcamaların denetlenmesi."
            },
            {
                "word": "objective",
                "vocab_id": "vocab.objective",
                "context_note_tr": "Altyapı maliyetlerini düşürme ve tasarruf hedefleri."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_fin_01",
                "By what percentage did the cloud infrastructure expenditure increase last quarter?",
                "Geçen çeyrekte bulut altyapı harcaması yüzde kaç artış gösterdi?",
                "Twenty-eight percent",
                [
                    "Five percent",
                    "Seventy-five percent",
                    "Exactly two percent"
                ],
                "Catherine states: 'our cloud infrastructure bill increased by twenty-eight percent last quarter.'",
                "Catherine faturanın geçen çeyrekte %28 arttığını söyler."
            ),
            build_q(
                "q_b2_fin_02",
                "What was identified as the primary driver of cloud budget overruns?",
                "Bulut bütçe aşımının ana etkeni olarak ne belirlendi?",
                "Staging and load-testing clusters running continuously on-demand without shutting down",
                [
                    "Excessive long-distance telephone calls made from corporate offices",
                    "Purchasing thousands of physical hard drives for employee homes",
                    "Massive cryptocurrency mining performed by external hackers"
                ],
                "Sinan notes staging and load-testing environments running 24/7 on on-demand instances drove the costs.",
                "Sinan test ortamlarının 7/24 talep üzerine çalışmasının maliyeti artırdığını belirtir."
            ),
            build_q(
                "q_b2_fin_03",
                "What automated policy does Sinan propose to curb non-production waste?",
                "Sinan üretim dışı israfı dizginlemek için hangi otomatik politikayı önermektedir?",
                "Scaling staging replicas down to zero automatically after seven p.m. using cron schedules",
                [
                    "Permanently deleting all staging environments and only testing directly in production",
                    "Charging developers ten dollars for every server instance they launch",
                    "Turning off the office building electricity every evening at five p.m."
                ],
                "Sinan proposes an automated cron schedule to scale staging replicas down to zero after 7 p.m.",
                "Sinan saat 19:00'dan sonra staging kopyalarını sıfıra indirecek zamanlayıcı önerir."
            ),
            build_q(
                "q_b2_fin_04",
                "How much monthly savings does Catherine anticipate from the non-production shutdown alone?",
                "Catherine yalnızca üretim dışı sistemlerin kapatılmasından aylık ne kadar tasarruf beklemektedir?",
                "Thirty thousand dollars monthly",
                [
                    "Five hundred dollars",
                    "One million dollars",
                    "Two thousand dollars"
                ],
                "Catherine estimates: 'That alone will save thirty thousand monthly.'",
                "Catherine bunun tek başına ayda otuz bin dolar tasarruf sağlayacağını belirtir."
            ),
            build_q(
                "q_b2_fin_05",
                "What additional discount rate is unlocked by committing to a three-year savings plan?",
                "Üç yıllık tasarruf planına taahhüt vermek hangi ek indirim oranını açmaktadır?",
                "An additional thirty-five percent discount",
                [
                    "An additional five percent discount",
                    "A ninety percent reduction",
                    "No financial discount at all"
                ],
                "Sinan confirms that a 3-year commitment yields an additional 35% discount on steady baseline instances.",
                "Sinan üç yıllık taahhüdün ek yüzde otuz beş indirim sağlayacağını açıklar."
            )
        ],
        "topic_tags": ["finops", "cloud-computing", "cost-optimization", "infrastructure"],
        "related_ids": ["vocab.audit", "vocab.objective"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.monolith-to-microservices-strangler",
        "title": "Architecture Strategy: Strangler Fig Pattern Decomposition",
        "cefr_level": "B2",
        "category": "engineering_meeting",
        "scenario_context": "Defne, a staff backend engineer, discusses breaking up a legacy billing system with Craig, the principal systems architect, using the Strangler Fig pattern.",
        "speakers": [
            {"id": "defne", "name": "Defne", "role": "Staff Backend Engineer", "accent": "Turkish"},
            {"id": "craig", "name": "Craig", "role": "Principal Systems Architect", "accent": "British"}
        ],
        "audio_ref": "audio/listening/b2_strangler_pattern.mp3",
        "duration_seconds": 52,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "craig",
                "start_ms": 0,
                "end_ms": 6500,
                "text_en": "Defne, our executive stakeholders are pressuring us to migrate away from the seven-year-old billing monolith. How do you envision the roadmap?",
                "text_tr": "Defne, yönetim paydaşlarımız yedi yıllık faturalandırma monolitinden uzaklaşmamız için bize baskı yapıyor. Yol haritasını nasıl görüyorsun?"
            },
            {
                "index": 2,
                "speaker_id": "defne",
                "start_ms": 6900,
                "end_ms": 15800,
                "text_en": "We must strictly avoid a high-risk 'big bang' rewrite. I advocate adopting Martin Fowler's Strangler Fig pattern to decouple functionality incrementally.",
                "text_tr": "Kesinlikle yüksek riskli 'büyük patlama' (big bang) yeniden yazımından kaçınmalıyız. İşlevselliği kademeli olarak ayırmak için Martin Fowler'ın Strangler Fig desenini benimsemeyi savunuyorum."
            },
            {
                "index": 3,
                "speaker_id": "craig",
                "start_ms": 16200,
                "end_ms": 23500,
                "text_en": "That minimizes operational disruption. Which bounded context should we intercept and carve out first?",
                "text_tr": "Bu durum operasyonel kesintiyi en aza indirir. İlk önce hangi sınırlı etki alanını (bounded context) araya girip ayırmalıyız?"
            },
            {
                "index": 4,
                "speaker_id": "defne",
                "start_ms": 23900,
                "end_ms": 32800,
                "text_en": "The invoice generation service. We can configure our API gateway to route all invoice PDF requests to a new Go microservice while the monolith processes ledger writes.",
                "text_tr": "Fatura oluşturma servisi. API ağ geçidimizi tüm fatura PDF isteklerini yeni bir Go mikro servisine yönlendirecek şekilde yapılandırabiliriz, monolit ise muhasebe defteri yazımlarını işlemeye devam eder."
            },
            {
                "index": 5,
                "speaker_id": "craig",
                "start_ms": 33200,
                "end_ms": 42000,
                "text_en": "How will the new microservice stay synchronized with customer profile updates occurring inside the legacy database?",
                "text_tr": "Yeni mikro servis eski veritabanı içinde gerçekleşen müşteri profili güncellemeleriyle nasıl senkronize kalacak?"
            },
            {
                "index": 6,
                "speaker_id": "defne",
                "start_ms": 42400,
                "end_ms": 50500,
                "text_en": "We will implement change data capture using Debezium to stream database transaction logs into Kafka topics asynchronously.",
                "text_tr": "Veritabanı işlem günlüklerini Kafka konularına asenkron olarak aktarmak için Debezium kullanarak Değişiklik Verisi Yakalama (CDC) uygulayacağız."
            }
        ],
        "key_vocabulary": [
            {
                "word": "collaborate",
                "vocab_id": "vocab.collaborate",
                "context_note_tr": "Monolitik sistemleri modern mimarilere dönüştürürken ekipler arası iş birliği."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Kademeli mimari dönüşümle sistem kesintisizliğini koruma yeteneği."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_strang_01",
                "Why does Defne reject a 'big bang' complete rewrite of the billing system?",
                "Defne faturalandırma sisteminin 'büyük patlama' tarzında sıfırdan tamamen yeniden yazılmasını neden reddediyor?",
                "A big bang rewrite carries excessive operational and business risk compared to incremental migration",
                [
                    "Company developers are forbidden from writing new software code by corporate bylaws",
                    "The original billing system was written by the company's founder and cannot be touched",
                    "Software rewrites always cause computer monitors to physically implode"
                ],
                "Defne warns against a high-risk big bang rewrite, proposing the Strangler Fig pattern for incremental migration.",
                "Defne yüksek riskli büyük patlama yeniden yazımından kaçınmayı ve kademeli Strangler Fig desenini savunur."
            ),
            build_q(
                "q_b2_strang_02",
                "What architectural pattern does Defne advocate to decompose the monolith?",
                "Defne monoliti ayrıştırmak için hangi mimari deseni savunmaktadır?",
                "The Strangler Fig pattern",
                [
                    "The Waterfall Model",
                    "The Monolithic Blob pattern",
                    "The Random Shuffling technique"
                ],
                "Defne explicitly recommends Martin Fowler's Strangler Fig pattern.",
                "Defne açıkça Martin Fowler'ın Strangler Fig desenini önerir."
            ),
            build_q(
                "q_b2_strang_03",
                "Which bounded context will be extracted first as an independent microservice?",
                "Bağımsız bir mikro servis olarak ilk önce hangi sınırlı etki alanı çıkarılacaktır?",
                "The invoice generation service",
                [
                    "The corporate human resources directory",
                    "The internal office catering ordering menu",
                    "The warehouse parking garage barrier controller"
                ],
                "Defne identifies invoice generation as the initial candidate to decouple.",
                "Defne fatura oluşturma servisini ilk aday olarak belirler."
            ),
            build_q(
                "q_b2_strang_04",
                "How will incoming traffic be split between the legacy monolith and the new microservice?",
                "Gelen trafik eski monolit ile yeni mikro servis arasında nasıl bölünecektir?",
                "The API gateway will route invoice requests to the new service while routing ledger writes to the monolith",
                [
                    "Employees will manually flip physical electrical switches on the server racks",
                    "Users will be required to download two separate smartphone applications",
                    "All incoming network cables will be cut in half with scissors"
                ],
                "Defne explains that the API gateway will route invoice PDF requests to the new Go service.",
                "Defne API ağ geçidinin fatura isteklerini yeni Go servisine yönlendireceğini açıklar."
            ),
            build_q(
                "q_b2_strang_05",
                "What technology solution ensures data synchronization without modifying legacy write code?",
                "Eski yazma kodunu değiştirmeden veri senkronizasyonunu hangi teknoloji çözümü sağlar?",
                "Change Data Capture (CDC) with Debezium streaming transaction logs to Kafka",
                [
                    "Printing daily summary reports and scanning them back into the system",
                    "Sending automated SMS messages between software engineers",
                    "Using manual spreadsheet copy-pasting every midnight"
                ],
                "Defne states they will implement change data capture using Debezium and Kafka topics.",
                "Defne Debezium ve Kafka kullanarak Değişiklik Verisi Yakalama uygulayacaklarını belirtir."
            )
        ],
        "topic_tags": ["microservices", "strangler-pattern", "software-architecture", "refactoring"],
        "related_ids": ["vocab.collaborate", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.enterprise-sla-penalty-negotiation",
        "title": "Commercial Negotiation: Service Level Agreement Breach Penalty",
        "cefr_level": "B2",
        "category": "negotiation",
        "scenario_context": "Kerem, director of customer engineering, negotiates service credit remedies with Fiona, enterprise procurement lead, following an unplanned cloud disruption.",
        "speakers": [
            {"id": "kerem", "name": "Kerem", "role": "Director of Customer Engineering", "accent": "Turkish"},
            {"id": "fiona", "name": "Fiona", "role": "Enterprise Procurement Director", "accent": "British"}
        ],
        "audio_ref": "audio/listening/b2_sla_negotiation.mp3",
        "duration_seconds": 53,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "fiona",
                "start_ms": 0,
                "end_ms": 6800,
                "text_en": "Kerem, last Tuesday's three-hour payment gateway downtime caused severe disruptions across our retail stores. Our SLA mandates a twenty percent billing credit.",
                "text_tr": "Kerem, geçen Salı günkü üç saatlik ödeme ağ geçidi kesintisi perakende mağazalarımızda ciddi aksamalara neden oldu. Hizmet Seviyesi Sözleşmemiz (SLA) yüzde yirmi fatura kredisi gerektiriyor."
            },
            {
                "index": 2,
                "speaker_id": "kerem",
                "start_ms": 7200,
                "end_ms": 15500,
                "text_en": "We deeply apologize for that incident, Fiona. We have published our comprehensive root-cause analysis explaining the third-party upstream fiber cut.",
                "text_tr": "O olay için çok özür dileriz Fiona. Üçüncü taraf sağlayıcıdaki fiber kesintisini açıklayan kapsamlı kök neden analizimizi yayımladık."
            },
            {
                "index": 3,
                "speaker_id": "fiona",
                "start_ms": 15900,
                "end_ms": 23500,
                "text_en": "We appreciate the transparency, but our contract clearly states that upstream vendor failures do not excuse availability commitments.",
                "text_tr": "Şeffaflığı takdir ediyoruz ancak sözleşmemiz açıkça üçüncü taraf tedarikçi arızalarının kullanılabilirlik taahhütlerini mazur göstermediğini belirtiyor."
            },
            {
                "index": 4,
                "speaker_id": "kerem",
                "start_ms": 23900,
                "end_ms": 32800,
                "text_en": "Understood. We are completely prepared to honor the twenty percent credit, which amounts to forty-five thousand dollars on this month's invoice.",
                "text_tr": "Anlaşıldı. Bu ayki faturada kırk beş bin dolara tekabül eden yüzde yirmilik krediyi eksiksiz olarak karşılamaya tamamen hazırız."
            },
            {
                "index": 5,
                "speaker_id": "fiona",
                "start_ms": 33200,
                "end_ms": 42000,
                "text_en": "Beyond the financial credit, what architectural redundancy are you deploying to ensure such an outage cannot recur during peak holiday shopping?",
                "text_tr": "Mali kredinin ötesinde yoğun tatil alışverişi döneminde böyle bir kesintinin tekrarlanmamasını sağlamak için hangi mimari yedekliliği devreye alıyorsunuz?"
            },
            {
                "index": 6,
                "speaker_id": "kerem",
                "start_ms": 42400,
                "end_ms": 51500,
                "text_en": "We are provisioning active-active multi-region failover across two distinct cloud regions, with automatic traffic rerouting completed in under five seconds.",
                "text_tr": "İki ayrı bulut bölgesi genelinde aktif-aktif çok bölgeli yük devretme kuruyoruz; otomatik trafik yeniden yönlendirmesi beş saniyenin altında tamamlanacak."
            }
        ],
        "key_vocabulary": [
            {
                "word": "customer",
                "vocab_id": "vocab.customer",
                "context_note_tr": "Kurumsal müşteri ilişkileri ve ticari sözleşme yükümlülükleri."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Hizmet kesintilerine karşı sunulan aktif-aktif çok bölgeli dayanıklılık."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_sla_01",
                "How long did the payment gateway service outage last on Tuesday?",
                "Salı günkü ödeme ağ geçidi hizmet kesintisi ne kadar sürdü?",
                "Three hours",
                [
                    "Fifteen minutes",
                    "Three full days",
                    "Ten seconds"
                ],
                "Fiona opens by noting: 'last Tuesday's three-hour payment gateway downtime.'",
                "Fiona üç saatlik kesintiden bahseder."
            ),
            build_q(
                "q_b2_sla_02",
                "What monetary value does the twenty percent contractual credit represent?",
                "Yüzde yirmilik sözleşme kredisi hangi parasal değere denk gelmektedir?",
                "Forty-five thousand dollars",
                [
                    "Five thousand dollars",
                    "One million dollars",
                    "Two hundred dollars"
                ],
                "Kerem states: 'twenty percent credit, which amounts to forty-five thousand dollars.'",
                "Kerem kredinin kırk beş bin dolara karşılık geldiğini belirtir."
            ),
            build_q(
                "q_b2_sla_03",
                "What external technical issue was identified in the vendor root-cause analysis?",
                "Tedarikçi kök neden analizinde hangi harici teknik sorun tespit edildi?",
                "A third-party upstream fiber optic cable cut",
                [
                    "An earthquake that swallowed the primary data center",
                    "A massive software virus designed by internal employees",
                    "A shortage of paper printing rolls inside retail stores"
                ],
                "Kerem notes the comprehensive root-cause analysis explained a third-party upstream fiber cut.",
                "Kerem üçüncü taraf sağlayıcıdaki fiber kesintisini açıklar."
            ),
            build_q(
                "q_b2_sla_04",
                "How does Fiona respond to the vendor's upstream excuse?",
                "Fiona tedarikçinin üçüncü taraf mazeretine nasıl yanıt vermektedir?",
                "She insists that upstream vendor failures do not excuse contractual availability commitments",
                [
                    "She agrees to cancel the contract immediately without payment",
                    "She apologizes to Kerem and offers to pay double the price",
                    "She demands that Kerem be removed from customer engineering"
                ],
                "Fiona points out that the contract clearly states upstream vendor failures do not excuse SLA commitments.",
                "Fiona sözleşmenin üçüncü taraf tedarikçi hatalarını mazeret saymadığını belirtir."
            ),
            build_q(
                "q_b2_sla_05",
                "What architectural measure will prevent similar outages during holiday shopping?",
                "Tatil alışverişi döneminde benzer kesintileri hangi mimari önlem engelleyecektir?",
                "Active-active multi-region failover with automatic traffic rerouting in under five seconds",
                [
                    "Hiring thirty human accountants to manually calculate credit card payments",
                    "Closing all retail stores for the entire month of December",
                    "Installing diesel generators inside every corporate office"
                ],
                "Kerem explains they are provisioning active-active multi-region failover with rerouting in under 5 seconds.",
                "Kerem iki bölgede 5 saniye altında yönlendirmeli aktif-aktif mimari kurduklarını belirtir."
            )
        ],
        "topic_tags": ["service-level-agreement", "negotiation", "disaster-recovery", "enterprise-contracts"],
        "related_ids": ["vocab.customer", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.feature-flag-rollout-strategy",
        "title": "Release Engineering: Canary Deployments and Feature Flags",
        "cefr_level": "B2",
        "category": "product_discovery",
        "scenario_context": "Pinar, a senior product manager, and Julian, release QA lead, design a progressive feature flag rollout for the new biometric authentication update.",
        "speakers": [
            {"id": "pinar", "name": "Pinar", "role": "Senior Product Manager", "accent": "Turkish"},
            {"id": "julian", "name": "Julian", "role": "Release QA Lead", "accent": "American"}
        ],
        "audio_ref": "audio/listening/b2_canary_rollout.mp3",
        "duration_seconds": 51,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "pinar",
                "start_ms": 0,
                "end_ms": 6500,
                "text_en": "Julian, the engineering team has wrapped up development on biometric face-unlock. How should we orchestrate the production rollout?",
                "text_tr": "Julian, mühendislik ekibi biyometrik yüz tanıma kilidi üzerindeki geliştirmeyi tamamladı. Canlıya çıkış sürecini (rollout) nasıl yönetmeliyiz?"
            },
            {
                "index": 2,
                "speaker_id": "julian",
                "start_ms": 6900,
                "end_ms": 15500,
                "text_en": "Given that biometric authentication touches sensitive user security, we should enforce a phased canary rollout using our LaunchDarkly feature flags.",
                "text_tr": "Biyometrik kimlik doğrulamanın hassas kullanıcı güvenliğine temas ettiği göz önüne alındığında, LaunchDarkly özellik bayraklarımızı (feature flags) kullanarak aşamalı bir 'canary' dağıtımı uygulamalıyız."
            },
            {
                "index": 3,
                "speaker_id": "pinar",
                "start_ms": 15900,
                "end_ms": 22800,
                "text_en": "Agreed. What user cohort percentages and monitoring duration do you recommend for each phase?",
                "text_tr": "Anlaştık. Her aşama için hangi kullanıcı kohort yüzdelerini ve izleme süresini öneriyorsun?"
            },
            {
                "index": 4,
                "speaker_id": "julian",
                "start_ms": 23200,
                "end_ms": 32000,
                "text_en": "Let's enable it for five percent of internal dogfooding users on day one, expand to twenty percent of production users on day three, and reach one hundred percent by day seven.",
                "text_tr": "Birinci gün şirket içi test kullanıcılarının yüzde beşine açalım, üçüncü gün canlı kullanıcıların yüzde yirmisine genişletelim ve yedinci günde yüzde yüze ulaşalım."
            },
            {
                "index": 5,
                "speaker_id": "pinar",
                "start_ms": 32400,
                "end_ms": 41200,
                "text_en": "What automated telemetry safeguards will trigger an immediate emergency rollback if an unexpected regression occurs?",
                "text_tr": "Beklenmedik bir gerileme (regression) meydana gelirse acil bir geri almayı (rollback) hangi otomatik telemetri korumaları tetikleyecek?"
            },
            {
                "index": 6,
                "speaker_id": "julian",
                "start_ms": 41600,
                "end_ms": 49800,
                "text_en": "If authentication error rates exceed zero point five percent or crash-free session rates drop below ninety-nine point nine, the flag automatically disables itself.",
                "text_tr": "Kimlik doğrulama hata oranları yüzde sıfır virgül beşi aşarsa veya çökmesiz oturum oranları yüzde doksan dokuz virgül dokuzun altına düşerse bayrak kendini otomatik olarak devre dışı bırakır."
            }
        ],
        "key_vocabulary": [
            {
                "word": "verify",
                "vocab_id": "vocab.verify",
                "context_note_tr": "Yeni özelliklerin kademeli dağıtımla telemetri üzerinden doğrulanması."
            },
            {
                "word": "vulnerability",
                "vocab_id": "vocab.vulnerability",
                "context_note_tr": "Hassas kimlik doğrulama akışlarındaki olası zafiyetlerin önlenmesi."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_roll_01",
                "What new feature is being prepared for progressive rollout?",
                "Kademeli canlıya çıkış için hangi yeni özellik hazırlanmaktadır?",
                "Biometric face-unlock authentication",
                [
                    "An automated cryptocurrency investment trading bot",
                    "A physical drone delivery tracking radar",
                    "An audio podcast streaming service"
                ],
                "Pinar introduces the topic: 'engineering team has wrapped up development on biometric face-unlock.'",
                "Pinar ekibin biyometrik yüz tanıma kilidi geliştirmesini tamamladığını söyler."
            ),
            build_q(
                "q_b2_roll_02",
                "Which feature flag management platform is being utilized for the rollout?",
                "Dağıtım için hangi özellik bayrağı (feature flag) yönetim platformu kullanılmaktadır?",
                "LaunchDarkly",
                [
                    "WordPress Admin",
                    "Google Sheets",
                    "Microsoft Paint"
                ],
                "Julian refers to: 'phased canary rollout using our LaunchDarkly feature flags.'",
                "Julian LaunchDarkly özellik bayraklarını kullanacaklarını belirtir."
            ),
            build_q(
                "q_b2_roll_03",
                "What rollout cohort percentage is scheduled for day three of production release?",
                "Canlı yayının üçüncü günü için hangi dağıtım kohort yüzdesi planlanmıştır?",
                "Twenty percent of production users",
                [
                    "Five percent",
                    "Fifty percent",
                    "One hundred percent"
                ],
                "Julian outlines: 'expand to twenty percent of production users on day three.'",
                "Julian 3. gün canlı kullanıcıların %20'sine genişletileceğini ifade eder."
            ),
            build_q(
                "q_b2_roll_04",
                "What error rate threshold triggers an automated feature flag kill-switch?",
                "Otomatik özellik bayrağı acil kapatma düğmesini (kill-switch) hangi hata oranı eşiği tetikler?",
                "If authentication error rates exceed zero point five percent",
                [
                    "If error rates exceed twenty-five percent",
                    "If any single user complains on social media",
                    "If the battery life of smartphones drops by ten percent"
                ],
                "Julian specifies: 'If authentication error rates exceed zero point five percent... flag automatically disables itself.'",
                "Julian hata oranının %0,5'i aşması durumunda bayrağın kapanacağını açıklar."
            ),
            build_q(
                "q_b2_roll_05",
                "What minimum crash-free session rate is required to keep the flag active?",
                "Bayrağın aktif kalması için hangi asgari çökmesiz oturum oranı şarttır?",
                "Ninety-nine point nine percent crash-free sessions",
                [
                    "Fifty percent",
                    "Eighty percent",
                    "Ninety percent"
                ],
                "Julian confirms: 'or crash-free session rates drop below ninety-nine point nine.'",
                "Julian oranın %99,9'un altına düşmemesi gerektiğini belirtir."
            )
        ],
        "topic_tags": ["canary-deployment", "feature-flags", "release-engineering", "quality-assurance"],
        "related_ids": ["vocab.verify", "vocab.vulnerability"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.event-driven-message-broker-selection",
        "title": "System Design: Kafka Partitioning vs RabbitMQ AMQP",
        "cefr_level": "B2",
        "category": "engineering_meeting",
        "scenario_context": "Onur, a platform engineer, debates message broker architectures with Natalie, the data streaming architect, for real-time logistics tracking.",
        "speakers": [
            {"id": "onur", "name": "Onur", "role": "Platform Engineer", "accent": "Turkish"},
            {"id": "natalie", "name": "Natalie", "role": "Data Streaming Architect", "accent": "British"}
        ],
        "audio_ref": "audio/listening/b2_message_broker.mp3",
        "duration_seconds": 53,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "onur",
                "start_ms": 0,
                "end_ms": 6800,
                "text_en": "Natalie, our current RabbitMQ broker is suffering memory backpressure as vehicle telemetry surges to eighty thousand GPS events per second.",
                "text_tr": "Natalie, araç telemetrisi saniyede seksen bin GPS olayına fırlarken mevcut RabbitMQ aracımız bellek geri basıncı (backpressure) yaşıyor."
            },
            {
                "index": 2,
                "speaker_id": "natalie",
                "start_ms": 7200,
                "end_ms": 15800,
                "text_en": "RabbitMQ is an excellent general-purpose message queue with rich routing, but it stores unacknowledged messages in RAM, which creates severe bottlenecks at high volumes.",
                "text_tr": "RabbitMQ zengin yönlendirmeye sahip mükemmel bir genel amaçlı mesaj kuyruğudur ancak onaylanmamış mesajları RAM'de saklar, bu da yüksek hacimlerde ciddi darboğazlar yaratır."
            },
            {
                "index": 3,
                "speaker_id": "onur",
                "start_ms": 16200,
                "end_ms": 23200,
                "text_en": "Are you suggesting we migrate our GPS location ingestion pipeline to an Apache Kafka cluster?",
                "text_tr": "GPS konum alım hattımızı bir Apache Kafka kümesine taşımamızı mı öneriyorsun?"
            },
            {
                "index": 4,
                "speaker_id": "natalie",
                "start_ms": 23600,
                "end_ms": 32800,
                "text_en": "Precisely. Kafka utilizes an append-only distributed commit log written sequentially to disk, providing orders of magnitude higher throughput and horizontal scalability.",
                "text_tr": "Kesinlikle. Kafka, diske sıralı olarak yazılan yalnızca eklemeli (append-only) dağıtık bir kayıt günlüğü kullanır; bu da kat kat daha yüksek verim ve yatay ölçeklenebilirlik sağlar."
            },
            {
                "index": 5,
                "speaker_id": "onur",
                "start_ms": 33200,
                "end_ms": 42000,
                "text_en": "How should we partition the Kafka topics to ensure that GPS coordinates for a specific delivery vehicle are processed in strict chronological order?",
                "text_tr": "Belirli bir teslimat aracına ait GPS koordinatlarının kesin kronolojik sırada işlenmesini sağlamak için Kafka konularını nasıl bölümlemeliyiz (partitioning)?"
            },
            {
                "index": 6,
                "speaker_id": "natalie",
                "start_ms": 42400,
                "end_ms": 51500,
                "text_en": "We will use the unique vehicle UUID as the Kafka partition key. That guarantees that all updates for any single vehicle land on the exact same partition.",
                "text_tr": "Kafka bölümleme anahtarı (partition key) olarak benzersiz araç UUID'sini kullanacağız. Bu tek bir araca ait tüm güncellemelerin tam olarak aynı bölüme düşmesini garanti eder."
            }
        ],
        "key_vocabulary": [
            {
                "word": "bottleneck",
                "vocab_id": "vocab.bottleneck",
                "context_note_tr": "Yüksek veri hacminde kuyruk sistemlerinde oluşan bellek darboğazı."
            },
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "Gerçek zamanlı olay akışlarında veri iletim gecikmesi."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_brok_01",
                "What data volume is overwhelming the existing RabbitMQ message broker?",
                "Mevcut RabbitMQ mesaj aracını zorlayan veri hacmi nedir?",
                "Eighty thousand GPS events per second",
                [
                    "Five emails per day",
                    "Ten thousand database backups per month",
                    "Three text messages per minute"
                ],
                "Onur reports telemetry surging to 80,000 GPS events per second.",
                "Onur telemetrinin saniyede 80.000 GPS olayına çıktığını bildirir."
            ),
            build_q(
                "q_b2_brok_02",
                "Why does RabbitMQ struggle under extreme high-throughput telemetry streams?",
                "RabbitMQ aşırı yüksek verimli telemetri akışları altında neden zorlanmaktadır?",
                "It retains unacknowledged messages in RAM, which creates severe memory backpressure",
                [
                    "It requires developers to hand-draw network diagrams for every message",
                    "It charges twenty dollars for every message processed by the system",
                    "It operates exclusively on obsolete analog telephone modems"
                ],
                "Natalie explains that RabbitMQ stores unacknowledged messages in RAM, creating severe bottlenecks.",
                "Natalie RabbitMQ'nun onaylanmamış mesajları RAM'de tutarak darboğaz yarattığını belirtir."
            ),
            build_q(
                "q_b2_brok_03",
                "What structural design allows Apache Kafka to achieve superior throughput?",
                "Apache Kafka'nın üstün verim elde etmesini hangi yapısal tasarım sağlamaktadır?",
                "An append-only distributed commit log written sequentially to disk",
                [
                    "Storing all messages in an unencrypted Microsoft Word file",
                    "Sending data using optical lasers pointed at the sky",
                    "Compressing all messages by deleting ninety percent of the numbers"
                ],
                "Natalie points out that Kafka uses an append-only distributed commit log written sequentially to disk.",
                "Natalie Kafka'nın diske sıralı yazılan eklemeli bir kayıt günlüğü kullandığını açıklar."
            ),
            build_q(
                "q_b2_brok_04",
                "How will the team ensure strict ordering for events originating from a single vehicle?",
                "Ekip tek bir araçtan gelen olaylar için kesin sıralamayı nasıl garanti edecektir?",
                "By using the vehicle UUID as the Kafka partition key",
                [
                    "By running only one computer server for the entire world",
                    "By waiting twelve hours between sending each GPS coordinate",
                    "By asking vehicle drivers to call headquarters verbally"
                ],
                "Natalie confirms that using the unique vehicle UUID as partition key guarantees ordered processing on the same partition.",
                "Natalie araç UUID'sini bölümleme anahtarı yaparak aynı bölümde sıralı işleme sağlanacağını açıklar."
            ),
            build_q(
                "q_b2_brok_05",
                "What specific domain is this streaming architecture designed to support?",
                "Bu veri akış mimarisi hangi spesifik alanı desteklemek üzere tasarlanmaktadır?",
                "Real-time logistics vehicle telemetry tracking",
                [
                    "Online multiplayer video game graphics rendering",
                    "Automated hospital surgical robotic arm controls",
                    "Undersea submarine acoustic sonar communications"
                ],
                "The context and dialogue explicitly address vehicle fleet telemetry and GPS tracking coordinates.",
                "Bağlam ve diyalog açıkça araç filosu telemetrisi ve GPS takibini ele almaktadır."
            )
        ],
        "topic_tags": ["system-design", "kafka", "rabbitmq", "event-driven-architecture", "streaming"],
        "related_ids": ["vocab.bottleneck", "vocab.latency"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.security-vulnerability-bounty-triage",
        "title": "Security Incident: SSRF Vulnerability Triage and Hotfix",
        "cefr_level": "B2",
        "category": "incident_response",
        "scenario_context": "Sarp, an application security engineer, alerts Elena, principal infrastructure lead, about a critical Server-Side Request Forgery vulnerability in the document preview service.",
        "speakers": [
            {"id": "sarp", "name": "Sarp", "role": "Application Security Lead", "accent": "Turkish"},
            {"id": "elena", "name": "Elena", "role": "Principal Infrastructure Lead", "accent": "American"}
        ],
        "audio_ref": "audio/listening/b2_ssrf_triage.mp3",
        "duration_seconds": 52,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "sarp",
                "start_ms": 0,
                "end_ms": 6500,
                "text_en": "Elena, we received a critical severity submission through our HackerOne bug bounty program thirty minutes ago.",
                "text_tr": "Elena, otuz dakika önce HackerOne ödül programımız üzerinden kritik önem derecesinde bir bildirim aldık."
            },
            {
                "index": 2,
                "speaker_id": "elena",
                "start_ms": 6900,
                "end_ms": 14200,
                "text_en": "What is the vulnerability classification and affected endpoint? Have we validated the researcher's proof of concept?",
                "text_tr": "Zafiyet sınıflandırması ve etkilenen uç nokta nedir? Araştırmacının kavram kanıtını (PoC) doğruladık mı?"
            },
            {
                "index": 3,
                "speaker_id": "sarp",
                "start_ms": 14600,
                "end_ms": 23500,
                "text_en": "It is an unauthenticated Server-Side Request Forgery on the PDF preview generator. The researcher demonstrated accessing the AWS cloud metadata IP.",
                "text_tr": "PDF önizleme oluşturucusunda kimlik doğrulamasız bir Sunucu Taraflı İstek Sahteciliği (SSRF). Araştırmacı AWS bulut meta veri IP'sine erişebildiğini gösterdi."
            },
            {
                "index": 4,
                "speaker_id": "elena",
                "start_ms": 23900,
                "end_ms": 32800,
                "text_en": "That is an acute risk because metadata services can expose IAM node credentials. Have we enforced IMDSv2 session tokens across that cluster?",
                "text_tr": "Bu çok ciddi bir risk çünkü meta veri servisleri IAM düğüm kimlik bilgilerini açığa çıkarabilir. O kümede IMDSv2 oturum belirteçlerini zorunlu kılmış mıydık?"
            },
            {
                "index": 5,
                "speaker_id": "sarp",
                "start_ms": 33200,
                "end_ms": 42000,
                "text_en": "IMDSv2 is enforced, which prevented the researcher from exfiltrating credentials. However, we must immediately block internal network requests.",
                "text_tr": "IMDSv2 zorunlu kılınmıştı, bu da araştırmacının kimlik bilgilerini dışarı sızdırmasını engelledi. Ancak dahili ağ isteklerini derhal engellemeliyiz."
            },
            {
                "index": 6,
                "speaker_id": "elena",
                "start_ms": 42400,
                "end_ms": 50500,
                "text_en": "I will deploy a network egress firewall rule blocking all traffic to 169.254.169.254, while you patch the URL parser with strict domain allowlisting.",
                "text_tr": "Sen URL ayrıştırıcısını katı alan adı beyaz listesiyle yamarken, ben 169.254.169.254'e giden tüm trafiği engelleyen bir ağ çıkış güvenlik duvarı kuralı dağıtacağım."
            }
        ],
        "key_vocabulary": [
            {
                "word": "vulnerability",
                "vocab_id": "vocab.vulnerability",
                "context_note_tr": "Yazılım ve bulut altyapısında keşfedilen güvenlik açığı."
            },
            {
                "word": "verify",
                "vocab_id": "vocab.verify",
                "context_note_tr": "Güvenlik araştırmacısının gönderdiği açığın doğrulanması."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_ssrf_01",
                "What specific type of security vulnerability was submitted via the bug bounty platform?",
                "Ödül platformu üzerinden hangi spesifik güvenlik zafiyeti bildirildi?",
                "Server-Side Request Forgery (SSRF) on the PDF preview generator",
                [
                    "A Cross-Site Scripting bug in the company blog comment section",
                    "A physical security badge clone used to enter the building garage",
                    "A denial-of-service attack on the office telephone lines"
                ],
                "Sarp identifies an unauthenticated Server-Side Request Forgery on the PDF preview generator.",
                "Sarp PDF önizleme oluşturucusunda kimlik doğrulamasız SSRF zafiyeti olduğunu bildirir."
            ),
            build_q(
                "q_b2_ssrf_02",
                "What sensitive internal resource was the security researcher able to reach?",
                "Güvenlik araştırmacısı hangi hassas dahili kaynağa erişebildi?",
                "The AWS cloud metadata IP service",
                [
                    "The personal laptop of the chief executive officer",
                    "The corporate cafeteria lunch ordering database",
                    "The local municipal electricity power grid"
                ],
                "Sarp states: 'The researcher demonstrated accessing the AWS cloud metadata IP.'",
                "Sarp araştırmacının AWS meta veri IP'sine eriştiğini belirtir."
            ),
            build_q(
                "q_b2_ssrf_03",
                "What prior security measure prevented the researcher from stealing IAM node credentials?",
                "Hangi önceki güvenlik önlemi araştırmacının IAM düğüm kimlik bilgilerini çalmasını engelledi?",
                "Enforced IMDSv2 session token requirements",
                [
                    "Turning off the internet every fifteen minutes",
                    "Encrypting the hard drive with a physical padlock",
                    "Removing all network cables from the computer servers"
                ],
                "Sarp confirms: 'IMDSv2 is enforced, which prevented the researcher from exfiltrating credentials.'",
                "Sarp IMDSv2'nin zorunlu kılınmış olmasının kimlik bilgilerinin sızdırılmasını engellediğini açıklar."
            ),
            build_q(
                "q_b2_ssrf_04",
                "What immediate network-level hotfix does Elena commit to deploying?",
                "Elena ağ düzeyinde hangi acil düzeltmeyi dağıtmayı taahhüt etmektedir?",
                "A network egress firewall rule blocking all traffic to 169.254.169.254",
                [
                    "Shutting down the entire cloud provider region indefinitely",
                    "Deleting all customer accounts created in the last month",
                    "Rewriting the entire cloud architecture in assembly language"
                ],
                "Elena confirms she will deploy an egress rule blocking traffic to 169.254.169.254.",
                "Elena 169.254.169.254 IP'sine giden trafiği engelleyen güvenlik duvarı kuralı koyacağını söyler."
            ),
            build_q(
                "q_b2_ssrf_05",
                "What application-level remediation will Sarp implement in the code?",
                "Sarp kod tarafında hangi uygulama düzeyinde iyileştirmeyi uygulayacaktır?",
                "Patching the URL parser with strict domain allowlisting",
                [
                    "Removing the PDF preview feature entirely from the product",
                    "Permitting users to enter any internal IP address they want",
                    "Allowing only administrative employees to read security logs"
                ],
                "Elena summarizes Sarp's task: 'while you patch the URL parser with strict domain allowlisting.'",
                "Elena Sarp'ın URL ayrıştırıcısını katı alan adı beyaz listesiyle yamayacağını belirtir."
            )
        ],
        "topic_tags": ["cybersecurity", "appsec", "ssrf-vulnerability", "incident-triage", "cloud-security"],
        "related_ids": ["vocab.vulnerability", "vocab.verify"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.b2.quarterly-engineering-okr-alignment",
        "title": "Executive Alignment: Balancing Feature Velocity and Technical Debt",
        "cefr_level": "B2",
        "category": "stakeholder_alignment",
        "scenario_context": "Alp, director of engineering, negotiates quarterly OKR resource allocations with Morgan, VP of Product, to secure capacity for refactoring.",
        "speakers": [
            {"id": "alp", "name": "Alp", "role": "Director of Engineering", "accent": "Turkish"},
            {"id": "morgan", "name": "Morgan", "role": "VP of Product", "accent": "American"}
        ],
        "audio_ref": "audio/listening/b2_okr_alignment.mp3",
        "duration_seconds": 54,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "morgan",
                "start_ms": 0,
                "end_ms": 6800,
                "text_en": "Alp, our Q3 business OKRs require shipping three major revenue features: automated billing invoicing, multi-currency support, and AI search.",
                "text_tr": "Alp, üçüncü çeyrek iş hedeflerimiz (OKR) üç büyük gelir getiren özelliği yayına almayı gerektiriyor: otomatik faturalandırma, çoklu para birimi desteği ve yapay zeka destekli arama."
            },
            {
                "index": 2,
                "speaker_id": "alp",
                "start_ms": 7200,
                "end_ms": 16200,
                "text_en": "Morgan, our engineering teams are eager to deliver customer value, but our legacy monolithic codebase has accumulated dangerous levels of architectural debt.",
                "text_tr": "Morgan, mühendislik ekiplerimiz müşteri değeri sunmaya son derece istekli ancak eski monolitik kod tabanımız tehlikeli seviyelerde mimari borç biriktirdi."
            },
            {
                "index": 3,
                "speaker_id": "morgan",
                "start_ms": 16600,
                "end_ms": 23500,
                "text_en": "I understand developer concerns, but if we don't launch multi-currency by August, we lose a multi-million-dollar European client.",
                "text_tr": "Geliştiricilerin endişelerini anlıyorum ancak Ağustos ayına kadar çoklu para birimini başlatamazsak milyonlarca dolarlık Avrupalı bir müşteriyi kaybederiz."
            },
            {
                "index": 4,
                "speaker_id": "alp",
                "start_ms": 23900,
                "end_ms": 33200,
                "text_en": "If we force multi-currency on top of our brittle database models without refactoring, our deployment lead times will double and production outages will escalate.",
                "text_tr": "Kod iyileştirme yapmadan çoklu para birimini kırılgan veritabanı modellerimizin üzerine zorlarsak dağıtım sürelerimiz iki katına çıkar ve canlı kesintiler tırmanır."
            },
            {
                "index": 5,
                "speaker_id": "morgan",
                "start_ms": 33600,
                "end_ms": 42000,
                "text_en": "What resource allocation split do you propose to ensure we meet the commercial deadline without crippling engineering health?",
                "text_tr": "Mühendislik sağlığını felce uğratmadan ticari teslim tarihini yakalamamızı sağlamak için nasıl bir kaynak tahsis dağılımı öneriyorsun?"
            },
            {
                "index": 6,
                "speaker_id": "alp",
                "start_ms": 42400,
                "end_ms": 52500,
                "text_en": "A strict seventy-thirty split: seventy percent throughput committed to product deliverables, and thirty percent dedicated to schema refactoring and CI automation.",
                "text_tr": "Kesin bir yetmiş-otuz dağılımı: İş gücünün yüzde yetmişi ürün teslimatlarına, yüzde otuzu ise şema iyileştirmelerine ve CI otomasyonuna ayrılacak."
            }
        ],
        "key_vocabulary": [
            {
                "word": "objective",
                "vocab_id": "vocab.objective",
                "context_note_tr": "Çeyreklik dönemde belirlenen ürün ve mühendislik OKR hedefleri."
            },
            {
                "word": "maintain",
                "vocab_id": "vocab.maintain",
                "context_note_tr": "Teknik borcu temizleyerek kod tabanının sürdürülebilirliğini korumak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_b2_okr_01",
                "What commercial risk does Morgan highlight if multi-currency support is delayed past August?",
                "Çoklu para birimi desteği Ağustos sonrasına ertelenirse Morgan hangi ticari riski vurgulamaktadır?",
                "The company risks losing a multi-million-dollar European enterprise client",
                [
                    "The company will be forced to file for immediate bankruptcy",
                    "All software developers will be prohibited from using credit cards",
                    "Government regulators will seize all office furniture"
                ],
                "Morgan warns: 'if we don't launch multi-currency by August, we lose a multi-million-dollar European client.'",
                "Morgan Ağustos'a kadar çıkılmazsa milyon dolarlık Avrupalı müşterinin kaybedileceğini belirtir."
            ),
            build_q(
                "q_b2_okr_02",
                "What technical consequence does Alp predict if new features are added without refactoring?",
                "Kod iyileştirme yapılmadan yeni özellikler eklenirse Alp hangi teknik sonucu öngörmektedir?",
                "Deployment lead times will double and production outages will escalate",
                [
                    "Computer monitors will display text entirely in reverse order",
                    "All developers will immediately forget how to write software",
                    "The cloud provider will double the electrical voltage to the servers"
                ],
                "Alp predicts that piling features onto brittle models will double deployment times and escalate outages.",
                "Alp kırılgan modeller üzerine geliştirme yapmanın teslimat sürelerini ikiye katlayacağını ve kesintileri artıracağını söyler."
            ),
            build_q(
                "q_b2_okr_03",
                "What specific resource allocation split does Alp negotiate?",
                "Alp hangi spesifik kaynak tahsis oranını müzakere etmektedir?",
                "Seventy percent for product features, and thirty percent for refactoring and CI automation",
                [
                    "One hundred percent for marketing and zero percent for software engineering",
                    "Fifty percent for sports activities and fifty percent for office cleaning",
                    "Ten percent for features and ninety percent for vacation time"
                ],
                "Alp proposes a strict 70/30 split between product deliverables and debt refactoring.",
                "Alp ürün teslimatları ile borç temizliği arasında net bir %70 / %30 dağılımı önerir."
            ),
            build_q(
                "q_b2_okr_04",
                "Which three product deliverables were demanded for Q3 by leadership?",
                "Yönetim tarafından 3. çeyrek için hangi üç ürün teslimatı talep edilmişti?",
                "Automated billing invoicing, multi-currency support, and AI search",
                [
                    "Free coffee machines, office swimming pools, and gaming consoles",
                    "Corporate private jets, luxury company cars, and designer suits",
                    "Paper encyclopedias, landline telephones, and postal stamps"
                ],
                "Morgan lists: automated billing invoicing, multi-currency support, and AI search.",
                "Morgan otomatik faturalama, çoklu para birimi ve yapay zeka aramasını sıralar."
            ),
            build_q(
                "q_b2_okr_05",
                "What is the ultimate purpose of dedicated technical debt capacity in this context?",
                "Bu bağlamda teknik borca ayrılan kapasitenin nihai amacı nedir?",
                "Preserving sustainable engineering velocity, schema health, and system reliability",
                [
                    "Giving software developers unlimited time to play video games",
                    "Delaying commercial product launches until competitors go out of business",
                    "Replacing all human developers with external automated scripts"
                ],
                "Both leaders agree that investing 30% in debt reduction protects long-term velocity and prevents catastrophic outages.",
                "Her iki lider de %30'luk yatırımın uzun vadeli geliştirme hızını ve sistem güvenilirliğini koruduğu konusunda uzlaşır."
            )
        ],
        "topic_tags": ["engineering-leadership", "okrs", "technical-debt", "product-strategy"],
        "related_ids": ["vocab.objective", "vocab.maintain"],
        "status": "APPROVED",
        "version": 1
    }
]

if __name__ == "__main__":
    print(f"Generated {len(B2_LISTENING_SCENARIOS)} B2 listening scenarios.")
    for s in B2_LISTENING_SCENARIOS:
        print(f"  [{s['cefr_level']}] {s['id']} - {s['title']} ({len(s['transcript_items'])} items, {len(s['comprehension_questions'])} questions)")
