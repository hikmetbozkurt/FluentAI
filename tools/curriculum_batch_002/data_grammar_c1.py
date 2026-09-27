#!/usr/bin/env python3
"""
Grammar Batch 002: C1 (13 lessons) definitions.
"""

from typing import List, Dict, Any

C1_LESSONS: List[Dict[str, Any]] = [
    {
        "id": "grammar.c1.subjunctive-in-formal-registers",
        "title": "The Mandative Subjunctive in Formal Governance and Corporate Policy",
        "cefr_level": "C1",
        "category": "modals_and_semi_modals",
        "summary_en": "Use the uninflected base verb (present subjunctive) in 'that'-clauses following verbs or adjectives of mandate, urgency, and recommendation.",
        "summary_tr": "Resmi kararlar, tavsiyeler ve gereklilik bildiren yapılardan sonra gelen 'that' cümleciğinde fiil şahıs eki almadan yalın (base form / be) kalır.",
        "explanation_en": [
            {
                "title": "Base Form Invariance in Subjunctive Complementation",
                "content": "In formal corporate governance, statutory drafting, and executive policy, clauses following verbs like 'demand', 'insist', 'recommend', 'stipulate', and adjectives like 'imperative', 'vital', 'crucial' take the base form of the verb regardless of subject person or grammatical tense: 'Management insists that every developer be given administrative oversight', 'It is imperative that she submit the quarterly compliance audit.'",
                "patterns": [
                    "insist / recommend / require + that + Subject + Base Verb (e.g., that he sign)",
                    "imperative / crucial / vital + that + Subject + be + V3 / Base Verb",
                    "Negative: that + Subject + not + Base Verb (without do/does/did)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'yapmasını talep etti / yapması şarttır' şeklindeki istek ve emir kiplerini karşılar. İngilizcede üçüncü tekil şahıs olsa bile fiil '-s' takısı almaz ('that he provide', 'provides' değil). 'To be' fiili ise am/is/are/was yerine doğrudan 'be' olarak kalır ('that the server be restarted'). Olumsuzunda 'do/does' kullanılmaz ('that he not deploy').",
        "rules": [
            {
                "name": "Mandative Subjunctive Invariance",
                "pattern": "Verb/Adj of Mandate + that + Subject + (not) + Base Verb",
                "use_cases": [
                    "Formulating organizational compliance policies and legal contractual covenants",
                    "Recording executive committee resolutions and binding board mandates"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The auditor recommends that each engineer update their credentials. (Subjunctive: base verb)",
                "structure_b": "The auditor recommends that each engineer updates their credentials. (Indicative: conversational UK/US)",
                "difference_explanation_en": "Structure A uses the formal mandative subjunctive 'update' without 3rd-person singular '-s'. Structure B uses indicative inflection, which is less formal.",
                "difference_explanation_tr": "A yapısı '-s' takısı almayan resmi istek kipi 'update' kullanır. B yapısı ise konuşma diline daha yakın bildirme kipidir."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Subjunctive Cümleciğinde 'Should' Zorunluluğu Zannetme",
                "explanation_tr": "Resmi İngilizcede 'that he should do' yerine doğrudan 'that he do' kalıbı esastır; fiile '-s' veya 'does' eklemek kural ihlalidir.",
                "incorrect_example": "The CTO demanded that the lead engineer reviews the security breach immediately.",
                "correct_example": "The CTO demanded that the lead engineer review the security breach immediately."
            }
        ],
        "examples": [
            {
                "en": "The regulatory board stipulated that all customer telemetry be stored within European borders.",
                "tr": "Düzenleyici kurul, tüm müşteri telemetri verilerinin Avrupa sınırları içinde saklanmasını şart koştu.",
                "rule_highlight": "stipulated that telemetry be stored (subjunctive passive 'be')",
                "context": "Compliance regulation"
            },
            {
                "en": "It is imperative that the lead architect not approve the release until latency tests conclude.",
                "tr": "Gecikme testleri sonuçlanana kadar baş mimarın sürümü onaylamaması zorunludur.",
                "rule_highlight": "imperative that the architect not approve (subjunctive negative)",
                "context": "Release governance"
            },
            {
                "en": "Our security policy mandates that every employee use hardware-based two-factor authentication.",
                "tr": "Güvenlik politikamız, her çalışanın donanım tabanlı iki adımlı kimlik doğrulama kullanmasını zorunlu kılar.",
                "rule_highlight": "mandates that every employee use (base verb)",
                "context": "Information security"
            },
            {
                "en": "The committee recommended that an independent third party conduct the penetration audit.",
                "tr": "Komite, sızma denetimini bağımsız bir üçüncü tarafın yürütmesini tavsiye etti.",
                "rule_highlight": "recommended that a third party conduct",
                "context": "Corporate governance"
            }
        ],
        "topic_tags": ["subjunctive", "mandative", "formal_register", "governance", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.inversion-with-prepositional-phrases",
        "title": "Negative and Restrictive Prepositional Inversion",
        "cefr_level": "C1",
        "category": "inversion_and_emphasis",
        "summary_en": "Prepositional phrases with negative or restrictive force (under no circumstances, on no account, in no way) trigger subject-auxiliary inversion when fronted.",
        "summary_tr": "Olumsuz veya kısıtlayıcı edat öbekleri (under no circumstances, at no time vb.) cümle başına alındığında yardımcı fiil öznenin önüne geçer.",
        "explanation_en": [
            {
                "title": "Fronted Prepositional Phrases and Subject-Auxiliary Inversion",
                "content": "When phrases such as 'under no circumstances', 'on no account', 'at no time', 'in no way', and 'to no degree' are placed at the beginning of a clause for rhetorical force or statutory clarity, the auxiliary verb must invert with the subject: 'Under no circumstances should production access be granted to unverified accounts.'",
                "patterns": [
                    "Under no circumstances + Modal / Auxiliary + Subject + Verb",
                    "On no account + Auxiliary + Subject + Verb",
                    "At no time + Auxiliary + Subject + Verb"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'hiçbir koşul altında ... yapılmamalıdır' yapısı vurgu için başa alınsa da yüklemdeki söz dizimi değişmez. İngilizcede ise 'Under no circumstances' başa geldiğinde cümle düz kurulamaz; soru cümlesi gibi devrik olmak zorundadır ('Under no circumstances you can' yanlıştır, 'can you' doğrudur).",
        "rules": [
            {
                "name": "Negative Prepositional Inversion Formula",
                "pattern": "Negative Prepositional Phrase + Aux/Modal + Subject + Base Verb",
                "use_cases": [
                    "Drafting zero-tolerance security directives and strict compliance rules",
                    "Writing formal audit findings and unequivocal disclaimers"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Production credentials must not be shared in public Slack channels under any circumstances.",
                "structure_b": "Under no circumstances must production credentials be shared in public Slack channels.",
                "difference_explanation_en": "Structure A is standard end-weight placement. Structure B is fronted rhetorical inversion, elevating regulatory authority and categorical prohibition.",
                "difference_explanation_tr": "A yapısı standart cümle sonu yerleşimidir. B yapısı ise kuralı en başa alıp devrik kurarak kesin bir yasaklama vurgusu oluşturur."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Olumsuz Edat Öbeğinden Sonra Düz Sıra Kullanma",
                "explanation_tr": "'Under no circumstances the system should restart' demek yanlıştır. Devrik yapı zorunludur: 'Under no circumstances should the system restart'.",
                "incorrect_example": "On no account developers may bypass the peer review gate.",
                "correct_example": "On no account may developers bypass the peer review gate."
            }
        ],
        "examples": [
            {
                "en": "Under no circumstances should customer cryptographic keys be stored in unencrypted memory.",
                "tr": "Hiçbir koşul altında müşteri şifreleme anahtarları şifrelenmemiş bellekte saklanmamalıdır.",
                "rule_highlight": "Under no circumstances should keys be stored",
                "context": "Cryptographic security"
            },
            {
                "en": "At no time did the operations team anticipate such an unprecedented traffic surge.",
                "tr": "Operasyon ekibi hiçbir zaman böylesine eşi benzeri görülmemiş bir trafik artışını öngörmemişti.",
                "rule_highlight": "At no time did the operations team anticipate",
                "context": "Incident retrospective"
            },
            {
                "en": "On no account will enterprise contracts be modified without prior legal review.",
                "tr": "Kurumsal sözleşmeler, önceden hukuki inceleme yapılmaksızın hiçbir surette değiştirilmeyecektir.",
                "rule_highlight": "On no account will contracts be modified",
                "context": "Legal procurement"
            },
            {
                "en": "In no way does this interim hotfix eliminate the need for an architectural redesign.",
                "tr": "Bu geçici acil yama, mimari bir yeniden tasarım ihtiyacını hiçbir şekilde ortadan kaldırmaz.",
                "rule_highlight": "In no way does this hotfix eliminate",
                "context": "Engineering leadership"
            }
        ],
        "topic_tags": ["inversion", "prepositional_phrases", "formal_emphasis", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.reduced-relative-clauses",
        "title": "Reduced Relative Clauses: Participle and Appositive Syntactic Compression",
        "cefr_level": "C1",
        "category": "relative_and_participle_clauses",
        "summary_en": "Omit relative pronouns and auxiliary verbs to reduce full relative clauses into concise present participle (-ing), past participle (-ed), or appositive adjective phrases.",
        "summary_tr": "İlgi zamirini ve yardımcı fiili atarak sıfat cümleciklerini kısaltılmış ortaç (-ing / -ed) ve apozitif sıfat öbeklerine dönüştürün.",
        "explanation_en": [
            {
                "title": "Mechanics of Relative Clause Reduction",
                "content": "Full relative clauses can be condensed into lean participial phrases by removing the relative pronoun and 'be': Active ('The microservice which handles telemetry' -> 'The microservice handling telemetry'), Passive ('The protocol which was drafted in 2021' -> 'The protocol drafted in 2021'), and Prepositional ('The developers who are on call' -> 'The developers on call'). This syntactic compression is standard in high-density technical and analytical prose.",
                "patterns": [
                    "Active Reduction: Noun + Verb-ing phrase (e.g., packets originating from...)",
                    "Passive Reduction: Noun + Past Participle (V3) phrase (e.g., components designated as critical)",
                    "Appositive Adjective Reduction: Noun + Adjective phrase (e.g., issues relevant to latency)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki sıfat-fiil (-en / -miş) yapılarına çok benzer: 'kaydedilen veriler' (data recorded), 'işlem yapan servis' (service processing). 'Which was recorded' yerine sadece 'recorded' demek cümleyi akıcı ve profesyonel kılar. Ancak etken eylemlerde '-ing', edilgenlerde ise 'V3' kullanılmasına dikkat edilmelidir.",
        "rules": [
            {
                "name": "Relative Reduction Rule",
                "pattern": "Noun + [who/which + be] + Participle -> Noun + Participle",
                "use_cases": [
                    "Compressing architecture descriptions and engineering specifications",
                    "Formulating concise executive summaries and technical patent claims"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Any customer who is experiencing authentication issues should clear their browser cache.",
                "structure_b": "Any customer experiencing authentication issues should clear their browser cache.",
                "difference_explanation_en": "Structure B eliminates 'who is', creating a reduced relative clause that delivers equivalent meaning with superior economy.",
                "difference_explanation_tr": "B yapısı 'who is' ifadesini atarak aynı anlamı daha yalın ve ekonomik bir dille verir."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Etken ile Edilgen Kısaltmayı Karıştırma",
                "explanation_tr": "Etken eylemlerde kısaltma '-ing' ile yapılır. 'The server hosted the application' yapısı kısaltılırken 'hosting' olur; 'hosted' edilgen kalır.",
                "incorrect_example": "We inspected the network packets sent from the malicious IP address.",
                "correct_example": "We inspected the network packets originating from the malicious IP address."
            }
        ],
        "examples": [
            {
                "en": "All microservices deployed to the primary European cluster must support mutual TLS authentication.",
                "tr": "Birincil Avrupa kümesine dağıtılan tüm mikro servisler karşılıklı TLS kimlik doğrulamasını desteklemelidir.",
                "rule_highlight": "microservices deployed to (reduced passive: which are deployed)",
                "context": "Cloud security"
            },
            {
                "en": "Engineers investigating high memory usage should examine the memory allocation graphs.",
                "tr": "Yüksek bellek kullanımını araştıran mühendisler, bellek tahsis grafiklerini incelemelidir.",
                "rule_highlight": "Engineers investigating (reduced active: who are investigating)",
                "context": "Debugging guideline"
            },
            {
                "en": "The proprietary consensus algorithm, developed originally in Zurich, guarantees high throughput.",
                "tr": "Orijinal olarak Zürih'te geliştirilen tescilli mutabakat algoritması, yüksek veri hacmini garanti eder.",
                "rule_highlight": "algorithm, developed originally in ... (reduced non-defining)",
                "context": "Distributed systems"
            },
            {
                "en": "Any pull request containing breaking database schema alterations requires dual sign-off.",
                "tr": "Bozucu veritabanı şeması değişiklikleri içeren her çekme isteği çift onay gerektirir.",
                "rule_highlight": "pull request containing (reduced active)",
                "context": "Governance policy"
            }
        ],
        "topic_tags": ["reduced_relative_clauses", "participles", "syntactic_compression", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.verbless-and-prepositional-clauses",
        "title": "Verbless Clauses and Syntactic Ellipsis in Analytical Prose",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Construct verbless clauses by omitting predictable subjects and the verb 'to be' after subordinators (if necessary, when feasible, whether intentional or not).",
        "summary_tr": "'If necessary', 'when feasible', 'whether intentional or not' gibi bağlaçlardan sonra tahmin edilebilir özne ve 'be' fiilini atarak fiilsiz yan cümlecikler (verbless clauses) kurun.",
        "explanation_en": [
            {
                "title": "Elliptical Subordination without Finite Verbs",
                "content": "In sophisticated academic and executive discourse, clauses introduced by conjunctions such as 'if', 'when', 'although', 'whether', and 'where' frequently drop the subject pronoun and copular 'be' when the subject is identical to the main clause subject or represents generic circumstances ('If necessary, reboot the cluster' -> 'If it is necessary...').",
                "patterns": [
                    "if / when + Adjective phrase (e.g., If applicable, when feasible)",
                    "whether X or Y (e.g., Whether deliberate or accidental, the breach must be investigated)",
                    "although / though + Adjective (e.g., Although computationally demanding, the cipher is secure)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'gerekirse', 'mümkün olduğunda', 'yorgun da olsa' şeklinde zarf-fiillerle kurulan yapıdır. İngilizcede 'If it is necessary' yerine doğrudan 'If necessary' denmesi cümleyi son derece akıcı, yetkin ve profesyonel kılar.",
        "rules": [
            {
                "name": "Verbless Ellipsis Condition",
                "pattern": "Subordinator (if/when/whether/though) + Adjective/Complement, Main Clause",
                "use_cases": [
                    "Authoring lean operational procedures and standard operating procedures (SOPs)",
                    "Balancing caveats and conditional provisions in technical whitepapers"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "When it is feasible, the team should parallelize database read operations.",
                "structure_b": "When feasible, the team should parallelize database read operations.",
                "difference_explanation_en": "Structure B achieves stylistic maturity by dropping the dummy 'it is' without sacrificing clarity.",
                "difference_explanation_tr": "B yapısı fazlalık 'it is' öbeklerini atarak netlikten ödün vermeden üslup olgunluğuna ulaşır."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Gereksiz Zamir ve Yardımcı Fiil Yükleme",
                "explanation_tr": "'If necessary' gibi yerleşmiş fiilsiz kalıplarda gereksizce 'If it will be necessary' demek Türk öğrencilerin sıkça yaptığı bir aktarım kusurudur.",
                "incorrect_example": "If it will be necessary, we will rollback the deployment.",
                "correct_example": "If necessary, we will rollback the deployment."
            }
        ],
        "examples": [
            {
                "en": "Although computationally demanding, the cryptographic proof guarantees absolute transaction finality.",
                "tr": "Hesaplama açısından zahmetli olsa da, kriptografik kanıt mutlak işlem kesinliğini garanti eder.",
                "rule_highlight": "Although computationally demanding (verbless concession)",
                "context": "Blockchain engineering"
            },
            {
                "en": "When feasible, the caching proxy serves stale content while refreshing the origin in the background.",
                "tr": "Mümkün olduğunda, önbellek vekili arka planda kaynağı yenilerken eski içeriği sunar.",
                "rule_highlight": "When feasible (verbless temporal condition)",
                "context": "CDN architecture"
            },
            {
                "en": "Whether deliberate or accidental, any exfiltration of customer records triggers an immediate forensic audit.",
                "tr": "İster kasıtlı ister kaza eseri olsun, müşteri kayıtlarının herhangi bir şekilde dışarı sızması derhal bir adli denetim başlatır.",
                "rule_highlight": "Whether deliberate or accidental (verbless alternative)",
                "context": "Information security"
            },
            {
                "en": "If applicable, include benchmark metrics in your pull request description.",
                "tr": "Varsa / geçerliyse, çekme isteği açıklamanıza kıyaslama metriklerini dahil edin.",
                "rule_highlight": "If applicable (verbless condition)",
                "context": "Engineering contribution guidelines"
            }
        ],
        "topic_tags": ["verbless_clauses", "ellipsis", "conciseness", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.modal-verbs-remote-probability-conjectural",
        "title": "Epistemic Modal Nuances: Remote Probability and Conjectural Stance",
        "cefr_level": "C1",
        "category": "modals_and_semi_modals",
        "summary_en": "Calibrate epistemic probability using nuanced modal collocations: 'might well' (high probability), 'could conceivably' (remote possibility), and 'should by rights' (expected standard).",
        "summary_tr": "'Might well' (kuvvetli olasılık), 'could conceivably' (uzak ihtimal) ve 'should by rights' (mantıken öyle olması gereken) gibi nüanslı modal öbeklerle olasılık derecesini ayarlayın.",
        "explanation_en": [
            {
                "title": "Epistemic Stance and Probabilistic Nuance",
                "content": "At C1, modals transcend basic ability or obligation. Native executive prose relies on idiomatic modal modifications: 'might well' suggests strong plausibility ('The competitor might well launch first'); 'could conceivably' introduces a speculative, low-probability scenario ('A distributed deadlock could conceivably occur under extreme loads'); 'should by rights' notes that logic or justice dictates an outcome ('The patch should by rights prevent further memory leaks').",
                "patterns": [
                    "Subject + might well / could well + Base Verb (high probability)",
                    "Subject + could conceivably + Base Verb (distant / theoretical possibility)",
                    "Subject + should by rights + Base Verb (expectation based on normative standards)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'büyük ihtimalle yapabilir', 'akla gelebilecek şekilde olabilir', 'hakkaniyet gereği olması gerekir' gibi kalıplardır. C1 seviyesinde yalnızca 'maybe' ya da 'might' demek yerine 'might well' (kuvvetle muhtemel) veya 'could conceivably' (uzak bir varsayım olarak) diyerek analitik duruşunuzu hassaslaştırabilirsiniz.",
        "rules": [
            {
                "name": "Modal Hedging Calibration",
                "pattern": "might well (likely) VS could conceivably (remote) VS should by rights (normative expectation)",
                "use_cases": [
                    "Evaluating architectural tail-risks and worst-case failure modes in design documents",
                    "Expressing guarded optimism or calibrated skepticism in executive strategy memos"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The migration might succeed. (Vague modal uncertainty)",
                "structure_b": "The migration might well exceed initial latency estimates. (Calculated, plausible likelihood)",
                "difference_explanation_en": "Adding 'well' elevates 'might' from ambiguous 50% doubt to a measured, probable risk scenario.",
                "difference_explanation_tr": "'Well' sözcüğü 'might'ı belirsiz bir tereddütten çıkarıp kuvvetli ve hesaplanmış bir risk tahminine dönüştürür."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "'Might Well' İfadesini 'İyi Yapmak' Olarak Anlama",
                "explanation_tr": "'Might well' bir şeyi iyi yapmak demek değildir; 'büyük ihtimalle öyle olması beklenir / kuvvetle muhtemeldir' anlamına gelen bir olasılık derecelendirmesidir.",
                "incorrect_example": "The database might well process, meaning it runs well.",
                "correct_example": "The database might well collapse under double the traffic."
            }
        ],
        "examples": [
            {
                "en": "Given the current database throughput bottlenecks, the impending flash sale might well trigger service degradation.",
                "tr": "Mevcut veritabanı veri hacmi darboğazları göz önüne alındığında, yaklaşan ani indirim satışı kuvvetle muhtemel servis performansını düşürebilir.",
                "rule_highlight": "might well trigger (high calculated probability)",
                "context": "Capacity planning"
            },
            {
                "en": "A malicious insider with physical hardware access could conceivably bypass multi-factor authentication.",
                "tr": "Fiziksel donanım erişimine sahip kötü niyetli bir kurum içi çalışan, teorik olarak çok faktörlü kimlik doğrulamayı atlatabilir.",
                "rule_highlight": "could conceivably bypass (remote conjectural possibility)",
                "context": "Threat modeling"
            },
            {
                "en": "This distributed lock mechanism should by rights eliminate the race condition completely.",
                "tr": "Bu dağıtık kilit mekanizmasının, mantıken yarış durumunu tamamen ortadan kaldırması gerekir.",
                "rule_highlight": "should by rights eliminate (normative expectation)",
                "context": "Concurrency engineering"
            },
            {
                "en": "The proposed micro-frontend architecture could well introduce severe bundle-size overhead.",
                "tr": "Önerilen mikro önyüz mimarisi, büyük olasılıkla ciddi bir paket boyutu yükü getirebilir.",
                "rule_highlight": "could well introduce",
                "context": "Architecture review"
            }
        ],
        "topic_tags": ["modals", "epistemic_stance", "hedging", "probability", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.cohesion-through-parallelism",
        "title": "Structural Parallelism and Balanced Coordination in Executive Prose",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Enforce identical grammatical forms across coordinates linked by correlative pairs (not only... but also, whether... or, neither... nor).",
        "summary_tr": "Bağlaçlarla birbirine bağlanan unsurların (not only... but also, whether... or vb.) aynı dilbilgisel yapıda (isim-isim, mastar-mastar) olmasını sağlayarak yapısal paralellik kurun.",
        "explanation_en": [
            {
                "title": "Symmetry and Cognitive Fluency in Argumentation",
                "content": "Faulty parallelism disrupts reader comprehension and signals unrefined writing. When linking elements with coordinating conjunctions or correlative pairs (not only X but also Y, both X and Y, either X or Y), both elements must share identical grammatical category: gerund with gerund, prepositional phrase with prepositional phrase, finite clause with finite clause.",
                "patterns": [
                    "Not only + Prepositional Phrase + but also + Prepositional Phrase",
                    "Not only + Verb Phrase + but also + Verb Phrase",
                    "Whether by + Gerund + or through + Gerund"
                ]
            }
        ],
        "explanation_tr": "Türkçede anlatım bozukluğu konusu olan paralel yapı uyumsuzluğudur ('Hem spor yapmayı hem de kitapları severim' yerine 'kitap okumayı'). İngilizcede 'not only... but also' kalıbının ilk tarafına fiil geliyorsa, ikinci tarafına da fiil gelmelidir; bir tarafa isim diğer tarafa cümle getirmek C1 seviyesinde kabul edilemez bir hatadır.",
        "rules": [
            {
                "name": "Correlative Parallelism Rule",
                "pattern": "not only [Grammatical Form A] but also [Identical Form A]",
                "use_cases": [
                    "Drafting formal project proposals, investment pitches, and strategic roadmaps",
                    "Formulating executive summary recommendations and mission statements"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The overhaul aimed not only to reduce cloud expenditure but also at boosting reliability. (FAULTY)",
                "structure_b": "The overhaul aimed not only to reduce cloud expenditure but also to boost reliability. (PARALLEL)",
                "difference_explanation_en": "Structure A mixes an infinitive 'to reduce' with a prepositional gerund 'at boosting'. Structure B aligns matching infinitives 'to reduce... to boost'.",
                "difference_explanation_tr": "A yapısı mastar ile edatlı fiilimsiyi karıştırarak paralelliği bozar. B yapısı ise iki tarafı da mastarla dengeleyerek mükemmel uyum sağlar."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Paralellik Bozukluğu ve Bağlaç Kayması",
                "explanation_tr": "'Not only' sözcüğünü fiilden önce koyup 'but also' sonrasına isim koymak ('He not only liked the code but also the author') paralellik hatasıdır. Fiil ortaksa bağlaç nesnelerin önüne geçmelidir: 'He liked not only the code but also the author'.",
                "incorrect_example": "The platform not only provides low latency but also security is guaranteed.",
                "correct_example": "The platform not only provides low latency but also guarantees robust security."
            }
        ],
        "examples": [
            {
                "en": "Our strategic initiative seeks not only to modernize our infrastructure but also to cultivate an engineering-led culture.",
                "tr": "Stratejik girişimimiz yalnızca altyapımızı modernize etmeyi değil, aynı zamanda mühendislik odaklı bir kültür geliştirmeyi de hedeflemektedir.",
                "rule_highlight": "not only to modernize ... but also to cultivate",
                "context": "Executive strategy"
            },
            {
                "en": "Engineers are evaluated whether on the elegance of their technical designs or on the clarity of their documentation.",
                "tr": "Mühendisler ister teknik tasarımlarının zarafetine göre, ister belgelerinin netliğine göre değerlendirilsin, yüksek standartlar aranır.",
                "rule_highlight": "whether on the elegance ... or on the clarity",
                "context": "Performance review criteria"
            },
            {
                "en": "The automated deployment pipeline was designed for deploying rapidly, testing continuously, and rolling back seamlessly.",
                "tr": "Otomatik dağıtım işlem hattı, hızla dağıtmak, sürekli test etmek ve sorunsuz bir şekilde geri almak için tasarlandı.",
                "rule_highlight": "deploying rapidly, testing continuously, and rolling back seamlessly (parallel gerunds)",
                "context": "DevOps design"
            },
            {
                "en": "We must decide whether to absorb the technical debt now or to refactor the module incrementally next sprint.",
                "tr": "Teknik borcu şimdi üstlenmek ile gelecek koşuda modülü kademeli olarak yeniden yapılandırmak arasında karar vermeliyiz.",
                "rule_highlight": "whether to absorb ... or to refactor",
                "context": "Sprint planning"
            }
        ],
        "topic_tags": ["parallelism", "cohesion", "coordination", "executive_prose", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.comparative-correlative-the-more-the-less",
        "title": "Comparative Correlatives: Proportional Structures with Inversion Nuances",
        "cefr_level": "C1",
        "category": "noun_phrases_and_articles",
        "summary_en": "Use paired comparative clauses ('The more..., the less...') to express proportional interdependence, optionally employing subject-verb inversion for stylistic weight.",
        "summary_tr": "Birbirine bağımlı orantılı durumları ifade etmek için 'The more..., the more/less...' kalıbı kullanılır; üslup ağırlığı için isteğe bağlı devrik yapı uygulanabilir.",
        "explanation_en": [
            {
                "title": "Proportional Interdependence and Elliptical Variants",
                "content": "The comparative correlative ('the... the...') expresses direct or inverse mathematical and functional relationships between two variables. Each half begins with 'the' followed by a comparative adjective, adverb, or quantifier phrase. While standard SVO follows ('The higher the concurrency, the greater the thread contention'), formal literary registers permit subject-verb inversion in the second clause ('The greater the load, the more fragile becomes the system').",
                "patterns": [
                    "The + comparative + Subject + Verb, the + comparative + Subject + Verb",
                    "The + comparative + Noun, the + comparative + Noun (verbless aphoristic form)",
                    "The + comparative + Subject + Verb, the + comparative + Verb + Subject (inversion)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Ne kadar ... o kadar ...' yapısıdır ('Ne kadar erken başlarsak o kadar çabuk biter'). İngilizcede her iki taraf da 'The + comparative' ile başlar ('The earlier we start, the sooner we finish'). İkinci tarafta 'the' unutulmamalı ve her iki taraftaki karşılaştırma sözcükleri simetrik kurulmalıdır.",
        "rules": [
            {
                "name": "Correlative Comparison Rule",
                "pattern": "The + [comparative element] + [clause 1], the + [comparative element] + [clause 2]",
                "use_cases": [
                    "Explaining system scaling dynamics, latency degradation, and algorithmic complexity",
                    "Formulating organizational trade-offs in economic and technical whitepapers"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "As the throughput increases, the latency increases as well.",
                "structure_b": "The higher the throughput, the greater the latency.",
                "difference_explanation_en": "Structure A is a loose descriptive clause. Structure B is a punchy, sophisticated comparative correlative expressing exact mechanical proportionality.",
                "difference_explanation_tr": "A yapısı sıradan bir zaman/neden cümlesidir. B yapısı ise doğrudan orantısal mekanizmayı vurgulayan yetkin bir karşılaştırma kalıbıdır."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "İkinci 'The' Belirtecini Atma Hatası",
                "explanation_tr": "İkinci cümlenin başındaki 'the' kesinlikle atlanamaz. 'The more you test, better it is' yanlıştır; 'the better it is' denmelidir.",
                "incorrect_example": "The faster the network interface, lower the transmission latency.",
                "correct_example": "The faster the network interface, the lower the transmission latency."
            }
        ],
        "examples": [
            {
                "en": "The more distributed an architecture becomes, the more difficult it is to guarantee data consistency.",
                "tr": "Bir mimari ne kadar dağıtık hale gelirse, veri tutarlılığını garanti etmek o kadar zorlaşır.",
                "rule_highlight": "The more distributed ... the more difficult",
                "context": "Distributed systems"
            },
            {
                "en": "The earlier we detect security vulnerabilities in the development lifecycle, the less costly they are to rectify.",
                "tr": "Geliştirme yaşam döngüsünde güvenlik açıklarını ne kadar erken tespit edersek, düzeltilmeleri o kadar az maliyetli olur.",
                "rule_highlight": "The earlier we detect ... the less costly",
                "context": "Shift-left security"
            },
            {
                "en": "The higher the database shard density, the more complex becomes the cross-partition query logic.",
                "tr": "Veritabanı parça yoğunluğu ne kadar yüksek olursa, bölümler arası sorgu mantığı o kadar karmaşık hale gelir.",
                "rule_highlight": "the more complex becomes the logic (inversion in second clause)",
                "context": "Database architecture"
            },
            {
                "en": "The tighter the project delivery deadline, the greater the temptation to accumulate technical debt.",
                "tr": "Proje teslim tarihi ne kadar sıkışık olursa, teknik borç biriktirme eğilimi o kadar büyük olur.",
                "rule_highlight": "The tighter ... the greater",
                "context": "Engineering management"
            }
        ],
        "topic_tags": ["comparative_correlative", "the_more_the_less", "proportionality", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.discourse-hedging-adverbials",
        "title": "Discourse Hedging Adverbials: Stance and Epistemic Commitment",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Employ stance adverbials (admittedly, arguably, ostensibly, purportedly, invariably) to modulate authorial commitment and demonstrate intellectual humility.",
        "summary_tr": "Analitik metinlerde yazarın iddiaya olan bağlılığını ve nesnelliğini ayarlamak için tutum zarfları (arguably, ostensibly, admittedly vb.) kullanılır.",
        "explanation_en": [
            {
                "title": "Nuances of Epistemic Adverbials in Critical Analysis",
                "content": "Senior professionals rarely make absolute, unhedged claims. Discourse adverbials signal epistemic positioning: 'arguably' presents a defensible interpretation that others might challenge; 'admittedly' concedes a counterpoint or vulnerability; 'ostensibly' signals that the apparent reason may disguise a deeper truth; 'purportedly' distances the speaker from unverified claims; 'invariably' denotes near-universal consistency.",
                "patterns": [
                    "Sentence-initial: Arguably, this is the most resilient consensus protocol.",
                    "Mid-position: The vendor was ostensibly upgrading the network, but caused an outage.",
                    "Concessive: Admittedly, this optimization introduces significant code complexity."
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'kuşkusuz', 'tartışmaya açık olmakla birlikte', 'sözde / görünüşte', 'iddia edildiğine göre' zarflarıdır. C1 düzeyinde bir mühendis veya yönetici 'This is the best solution' gibi ham iddialar yerine 'This is arguably the most scalable solution' veya 'Admittedly, latency will rise' diyerek analitik olgunluk sergiler.",
        "rules": [
            {
                "name": "Epistemic Adverbial Stance Calibration",
                "pattern": "Adverbial (Sentence initial / Mid-position) + Proposition",
                "use_cases": [
                    "Writing nuanced peer reviews, architectural evaluations, and RFP responses",
                    "Conducting postmortem analyses without assigning reckless definitive blame"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Our microservice architecture is the best design in the industry. (Naive / Dogmatic)",
                "structure_b": "Our microservice architecture is arguably the most resilient design in our sector. (Defensible C1 hedge)",
                "difference_explanation_en": "Structure B uses 'arguably' to elevate the claim into a well-reasoned, defensible intellectual judgment rather than dogmatic boasting.",
                "difference_explanation_tr": "B yapısı 'arguably' kullanarak ham bir övünme yerine savunulabilir, olgun ve saygın bir analitik iddia ortaya koyar."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "'Arguably' Kelimesini 'Tartışmalı/Şüpheli' Olarak Çevirme",
                "explanation_tr": "'Arguably' kelimesi 'şüpheli' demek değildir; 'sağlam argümanlarla savunulabilir ki' anlamına gelir ve genellikle güçlü bir övgü veya tespittir.",
                "incorrect_example": "The design is arguably, so we must reject it.",
                "correct_example": "This is arguably the most robust encryption standard available today."
            }
        ],
        "examples": [
            {
                "en": "Admittedly, the initial cloud migration generated higher operational costs than originally forecast.",
                "tr": "Kabul etmek gerekir ki, ilk bulut geçişi başlangıçta tahmin edilenden daha yüksek operasyonel maliyetler yarattı.",
                "rule_highlight": "Admittedly, the initial migration (concessive stance)",
                "context": "Executive financial review"
            },
            {
                "en": "Rust is arguably the most suitable programming language for high-throughput, memory-safe systems.",
                "tr": "Rust, yüksek veri hacimli ve bellek açısından güvenli sistemler için kuvvetle muhtemel en uygun programlama dilidir.",
                "rule_highlight": "is arguably the most suitable",
                "context": "Technology evaluation"
            },
            {
                "en": "The vendor ostensibly performed scheduled maintenance, but internal logs reveal an unplanned kernel panic.",
                "tr": "Tedarikçi görünüşte planlı bakım gerçekleştirdi, ancak dahili kayıtlar plansız bir çekirdek çökmesi olduğunu ortaya koyuyor.",
                "rule_highlight": "ostensibly performed (apparent vs real)",
                "context": "Vendor audit"
            },
            {
                "en": "Aggressive database connection pooling invariably reduces latency under sustained traffic surges.",
                "tr": "Agresif veritabanı bağlantı havuzu oluşturma, sürekli trafik artışları altında istisnasız bir şekilde gecikmeyi azaltır.",
                "rule_highlight": "invariably reduces (consistent reliability)",
                "context": "Performance engineering"
            }
        ],
        "topic_tags": ["hedging", "discourse_markers", "epistemic_stance", "adverbials", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.it-clefts-for-contrastive-focus",
        "title": "It-Clefts for Narrow Contrastive Focus and Rectification",
        "cefr_level": "C1",
        "category": "cleft_sentences",
        "summary_en": "Use 'It is/was X that/who...' to isolate a specific constituent (subject, object, adverbial) and reject implicit competing alternatives.",
        "summary_tr": "Belirli bir unsuru (özne, nesne, yer veya zaman) öne çıkarıp olası yanlış varsayımları çürütmek için 'It is/was ... that' yapısı kullanılır.",
        "explanation_en": [
            {
                "title": "Exclusive Focal Cleaving and Correction",
                "content": "An it-cleft isolates a targeted element into the post-copular position ('It was X that...'), leaving the rest of the proposition as a presupposed relative clause. It is frequently employed to correct misconceptions or pinpoint root causality ('It was not the database configuration, but the network switch that failed').",
                "patterns": [
                    "It + is/was + Focused Element + that/who + Clause",
                    "It is/was + Not X but Y + that + Clause (contrastive rectification)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki '... yapan tam olarak şuydu' vurgusudur. 'Hata sunucudan kaynaklandı' demek yerine 'Soruna yol açan şey sunucu değil, ağ anahtarıydı' diyerek odak doğrudan hedeflenen noktaya çekilir ('It was the network switch that failed'). Vurgulanan şey insan olsa bile nesnel metinlerde 'that' yaygın olarak kullanılır.",
        "rules": [
            {
                "name": "It-Cleft Rectification Formula",
                "pattern": "It + be + [Focused Constituent] + that/who + [Background Proposition]",
                "use_cases": [
                    "Assigning precise root causality in incident postmortems without ambiguity",
                    "Clarifying intellectual property attribution and project ownership"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "A configuration drift caused the authentication failure.",
                "structure_b": "It was a configuration drift that caused the authentication failure.",
                "difference_explanation_en": "Structure A states the cause plainly. Structure B uses an it-cleft to establish that configuration drift—and nothing else—was responsible.",
                "difference_explanation_tr": "A yapısı sebebi düz biçimde anlatır. B yapısı ise it-cleft ile diğer tüm ihtimalleri dışlayarak tam olarak yapılandırma kaymasının sorumlu olduğunu vurgular."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "'It' Yerine 'This' veya 'That' ile Cleft Kurma",
                "explanation_tr": "Cleft yapısının kukla öznesi daima 'It' olmak zorundadır. 'This was the engineer that found the bug' cleft değildir; 'It was the engineer who found the bug' olmalıdır.",
                "incorrect_example": "This was our team that finalized the architecture roadmap.",
                "correct_example": "It was our team that finalized the architecture roadmap."
            }
        ],
        "examples": [
            {
                "en": "It was an unhandled null pointer exception in the payment gateway that precipitated the system crash.",
                "tr": "Sistem çökmesini tetikleyen şey, ödeme ağ geçidindeki yakalanmamış bir boş işaretçi istisnasıydı.",
                "rule_highlight": "It was an unhandled exception ... that precipitated",
                "context": "Incident postmortem"
            },
            {
                "en": "It is our European compliance director who holds the final veto over cross-border data transfers.",
                "tr": "Sınır ötesi veri transferleri üzerinde nihai veto yetkisine sahip olan kişi Avrupa uyumluluk direktörümüzdür.",
                "rule_highlight": "It is our director who holds",
                "context": "Corporate governance"
            },
            {
                "en": "It was not until the secondary audit concluded that the full extent of the data leak became evident.",
                "tr": "Veri sızıntısının tüm boyutu ancak ikinci denetim sonuçlandıktan sonra belirgin hale geldi.",
                "rule_highlight": "It was not until ... that (temporal focus)",
                "context": "Audit findings"
            },
            {
                "en": "It is precisely during peak traffic surges that graceful degradation algorithms prove their value.",
                "tr": "Zarif performans düşürme algoritmalarının değerini kanıtladığı an, tam olarak yoğun trafik artışlarının yaşandığı andır.",
                "rule_highlight": "It is precisely during peak surges that (adverbial focus)",
                "context": "System resilience"
            }
        ],
        "topic_tags": ["cleft_sentences", "it_clefts", "emphasis", "focus", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.subordinating-conjunctions-concession-condition",
        "title": "Specialized Subordinating Conjunctions: Inasmuch as, Insofar as, and Lest",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Deploy precise academic and legal subordinators ('inasmuch as', 'insofar as', 'provided that', 'lest') to establish tight logical bounds.",
        "summary_tr": "Mantıksal ve hukuki sınırları kesinleştirmek için 'inasmuch as' (mademki), 'insofar as' (-dığı kadarıyla) ve 'lest' (-mesin diye) gibi özelleşmiş bağlaçlar kullanılır.",
        "explanation_en": [
            {
                "title": "Precision Subordination in Formal Discourse",
                "content": "'Inasmuch as' introduces a known premise explaining a conclusion ('Inasmuch as the vendor breached the SLA, penalties apply'). 'Insofar as' specifies the scope or degree to which a statement holds true ('The architecture is scalable insofar as compute can be decoupled from storage'). 'Lest' expresses negative purpose or precaution (equivalent to 'for fear that' or 'so that... not'), traditionally paired with the subjunctive base verb or 'should' ('We created backups lest the disk crash').",
                "patterns": [
                    "Inasmuch as + Subject + Verb, Main Clause (causal premise)",
                    "Insofar as + Subject + Verb, Main Clause (limitation of scope)",
                    "lest + Subject + (should) + Base Verb (precaution against negative outcome)"
                ]
            }
        ],
        "explanation_tr": "Hukuki ve resmi dilde 'lest' bağlacı '-mesin diye / korkusuyla' anlamına gelir ve peşinden gelen fiil ya yalın kalır (subjunctive) ya da 'should' alır ('lest the system fail'). 'Insofar as' ise 'elverdiği ölçüde / kadarıyla' anlamında kapsam sınırlar. Bu bağlaçlar resmi metinlere üst düzey bir ciddiyet katar.",
        "rules": [
            {
                "name": "Advanced Subordination Constraints",
                "pattern": "inasmuch as (since/because) | insofar as (to the extent that) | lest + (should) + V1",
                "use_cases": [
                    "Formulating contractual scope clauses and terms of service limits",
                    "Articulating precautionary risk mitigations in high-assurance engineering"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "We logged every transaction so that we would not lose audit trails.",
                "structure_b": "We logged every transaction lest audit trails be lost.",
                "difference_explanation_en": "Structure B uses the elevated precautionary subordinator 'lest' with a subjunctive passive, creating an authoritative, formal tone.",
                "difference_explanation_tr": "B yapısı 'lest' ve istek kipi kullanarak daha üst düzey, kurumsal ve yetkin bir ihtiyati üslup oluşturur."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "'Lest' Sonrasında Olumsuz Fiil Kullanma",
                "explanation_tr": "'Lest' kendi içinde 'olmasın diye' anlamı taşıdığından, peşinden gelen cümle asla 'not' almaz. 'Lest the server does not fail' yanlıştır; 'lest the server fail' denmelidir.",
                "incorrect_example": "We monitored the cluster lest memory leaks would not occur.",
                "correct_example": "We monitored the cluster lest memory leaks occur."
            }
        ],
        "examples": [
            {
                "en": "Insofar as the cloud platform adheres to open standards, vendor lock-in risks remain manageable.",
                "tr": "Bulut platformu açık standartlara bağlı kaldığı ölçüde, tedarikçiye bağımlılık riskleri yönetilebilir kalır.",
                "rule_highlight": "Insofar as the platform adheres (scope limitation)",
                "context": "Cloud architecture strategy"
            },
            {
                "en": "The SRE lead instituted strict deployment freezes lest unverified changes compromise holiday uptime.",
                "tr": "SRE lideri, doğrulanmamış değişiklikler tatil çalışma süresini tehlikeye atmasın diye katı dağıtım dondurmaları uyguladı.",
                "rule_highlight": "lest unverified changes compromise (precautionary subjunctive)",
                "context": "Reliability governance"
            },
            {
                "en": "Inasmuch as the vendor failed to deliver the audit reports, the commercial contract stands void.",
                "tr": "Tedarikçi denetim raporlarını teslim edemediğinden ötürü / mademki edemedi, ticari sözleşme geçersiz sayılır.",
                "rule_highlight": "Inasmuch as the vendor failed (formal causal premise)",
                "context": "Legal contract dispute"
            },
            {
                "en": "Provided that our automated integration test suite passes, we will schedule the canary deployment for dusk.",
                "tr": "Otomatik entegrasyon test paketimizin başarılı olması şartıyla, kanarya dağıtımını akşam saatine planlayacağız.",
                "rule_highlight": "Provided that our suite passes",
                "context": "Release orchestration"
            }
        ],
        "topic_tags": ["subordination", "insofar_as", "lest", "inasmuch_as", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.noun-complement-clauses-fact-belief",
        "title": "Noun Complement Clauses vs. Relative Clauses with Abstract Nouns",
        "cefr_level": "C1",
        "category": "noun_phrases_and_articles",
        "summary_en": "Distinguish between noun complement clauses ('the hypothesis that X is true', complete proposition) and relative clauses ('the hypothesis that we tested', incomplete clause).",
        "summary_tr": "Soyut isimleri açıklayan isim tamamlama cümlecikleri (the fact that..., tam cümle) ile ilgi cümleciklerini (the fact that we know, eksik nesneli) birbirinden ayırt edin.",
        "explanation_en": [
            {
                "title": "Structural Content Delivery vs. Nominal Modification",
                "content": "Abstract cognitive and communication nouns (fact, hypothesis, assumption, belief, conclusion, evidence) take two types of 'that'-clauses. In a Noun Complement Clause, 'that' is an empty complementizer introducing a structurally complete sentence that defines the content of the noun ('The assumption that all microservices are stateless is false'). In a Relative Clause, 'that' is a pronoun replacing an argument inside an incomplete clause ('The assumption that we made yesterday was false'). Complement clauses cannot replace 'that' with 'which'.",
                "patterns": [
                    "Noun Complement: Abstract Noun + that + Complete Independent Clause (SVO)",
                    "Relative Clause: Abstract Noun + that/which + Incomplete Clause (missing S or O)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki '... olduğu gerçeği / varsayımı' yapısıdır. 'The assumption that all services are fast' cümlesinde 'that' bir ilgi zamiri değildir; cümlenin içi tamdır (öznesi, fiili, nesnesi vardır) ve 'which' ile değiştirilemez. Eğer cümlenin nesnesi eksikse ('The assumption that we tested'), o zaman ilgi cümleciğidir ve 'which' alabilir.",
        "rules": [
            {
                "name": "Complement Clause Completeness Test",
                "pattern": "Noun + that + Complete Proposition (cannot substitute 'which')",
                "use_cases": [
                    "Formulating analytical research hypotheses and validating system assumptions",
                    "Dissecting logical fallacies and untested premises in architecture reviews"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The conclusion that the database was overloaded proved accurate. (Noun complement: defines conclusion)",
                "structure_b": "The conclusion which the committee reached proved accurate. (Relative clause: modifies conclusion)",
                "difference_explanation_en": "In A, 'that...' is the internal content of the conclusion. In B, 'which...' is an external relative clause modifying the noun.",
                "difference_explanation_tr": "A'da 'that...' sonucun bizzat içeriğini açıklar. B'de ise 'which...' komitenin ulaştığı sonucu dışarıdan niteler."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "İsim Tamamlama Cümleciğinde 'Which' Kullanma Hatası",
                "explanation_tr": "'The fact which' kalıbı isim tamamlama yaparken kullanılamaz; içeriği tanımlayan tam cümle mutlaka 'that' ile bağlanmalıdır.",
                "incorrect_example": "The hypothesis which asynchronous messaging reduces coupling was verified.",
                "correct_example": "The hypothesis that asynchronous messaging reduces coupling was verified."
            }
        ],
        "examples": [
            {
                "en": "The foundational assumption that client devices operate in low-latency networks is frequently violated in mobile contexts.",
                "tr": "İstemci cihazların düşük gecikmeli ağlarda çalıştığı yönündeki temel varsayım, mobil bağlamlarda sıklıkla ihlal edilir.",
                "rule_highlight": "assumption that client devices operate (noun complement clause)",
                "context": "System architecture"
            },
            {
                "en": "There is compelling telemetry evidence that database connection pool exhaustion triggered the outage.",
                "tr": "Veritabanı bağlantı havuzunun tükenmesinin kesintiyi tetiklediğine dair ikna edici telemetri kanıtı vardır.",
                "rule_highlight": "evidence that connection pool exhaustion triggered",
                "context": "Incident forensic analysis"
            },
            {
                "en": "Management rejected the notion that security audits hinder engineering delivery velocity.",
                "tr": "Yönetim, güvenlik denetimlerinin mühendislik teslim hızını engellediği fikrini reddetti.",
                "rule_highlight": "the notion that security audits hinder",
                "context": "Engineering culture"
            },
            {
                "en": "The team accepted the proposal that was submitted by the principal architect.",
                "tr": "Ekip, baş mimar tarafından sunulan teklifi kabul etti.",
                "rule_highlight": "proposal that was submitted (relative clause contrast)",
                "context": "Architecture decision"
            }
        ],
        "topic_tags": ["noun_complement", "abstract_nouns", "relative_clauses", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.complex-passive-with-prepositional-verbs",
        "title": "Complex Passives of Prepositional and Phrasal-Prepositional Verbs",
        "cefr_level": "C1",
        "category": "passive_and_causative",
        "summary_en": "Form passives with multi-word verbs where the final preposition or particle remains firmly attached to the participle (accounted for, catered to, dispensed with).",
        "summary_tr": "Edatlı ve öbeksi fiillerin edilgen yapılarında (accounted for, catered to vb.) edat fiilin hemen ardında asılı kalır.",
        "explanation_en": [
            {
                "title": "Particle Retention in Multi-Word Passives",
                "content": "When prepositional verbs (account for, look into, dispose of) and phrasal-prepositional verbs (do away with, put up with) are transformed into the passive voice, the preposition cannot be detached or relocated; it remains fused immediately after the past participle: 'All edge cases have been accounted for', 'Legacy microservices will be done away with next quarter.'",
                "patterns": [
                    "Subject + be + V3 + Preposition (e.g., The bug is being looked into)",
                    "Subject + be + V3 + Particle + Preposition (e.g., This practice must be done away with)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'hesaba katılmak', 'ilgi gösterilmek' gibi tek bir birleşik eylem oluşturulur. İngilizcede ise 'account for' edilgen yapıldığında 'for' edatı fiilin hemen peşine takılı kalır: 'All details were accounted for'. Türk öğrenciler edatı cümlenin sonuna bırakmaktan rahatsız olup düşürme eğilimi gösterirler; ancak edat düşerse cümle anlamsızlaşır.",
        "rules": [
            {
                "name": "Prepositional Passive Particle Retention",
                "pattern": "Subject + be + V3 + Preposition/Particle",
                "use_cases": [
                    "Summarizing ticket statuses, unresolved incidents, and technical audit findings",
                    "Describing obsolete tooling deprecation and architectural phase-outs"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The security team looked into the anomaly thoroughly.",
                "structure_b": "The anomaly was thoroughly looked into by the security team.",
                "difference_explanation_en": "In passive structure B, the preposition 'into' remains welded to the participle 'looked', completing the phrasal verb meaning.",
                "difference_explanation_tr": "B edilgen yapısında 'into' edatı 'looked' fiiline kenetlenmiş olarak kalır ve deyimsel anlamı tamamlar."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "Edilgen Fiilin Sonundaki Edatı Düşürme Hatası",
                "explanation_tr": "'The requirements were catered to' cümlesinden 'to' edatını atmak yanlıştır. Edat fiilin anlamının ayrılmaz bir parçasıdır.",
                "incorrect_example": "All customer complaints have been dealt prompt.",
                "correct_example": "All customer complaints have been dealt with promptly."
            }
        ],
        "examples": [
            {
                "en": "Every potential race condition in the distributed queue has been rigorously accounted for.",
                "tr": "Dağıtık kuyruktaki her potansiyel yarış durumu titizlikle hesaba katılmıştır.",
                "rule_highlight": "accounted for (passive with preposition)",
                "context": "Concurrency engineering"
            },
            {
                "en": "The suspicious network traffic spike is currently being looked into by our cybersecurity analysts.",
                "tr": "Şüpheli ağ trafiği artışı şu anda siber güvenlik analistlerimiz tarafından incelenmektedir.",
                "rule_highlight": "is currently being looked into",
                "context": "Threat intelligence"
            },
            {
                "en": "Manual database migration scripts will be completely done away with after this release.",
                "tr": "Bu sürümden sonra manuel veritabanı geçiş betikleri tamamen ortadan kaldırılacaktır.",
                "rule_highlight": "done away with (phrasal-prepositional passive)",
                "context": "DevOps modernization"
            },
            {
                "en": "The accessibility needs of visually impaired users must be thoroughly catered to.",
                "tr": "Görme engelli kullanıcıların erişilebilirlik ihtiyaçları eksiksiz bir şekilde karşılanmalıdır.",
                "rule_highlight": "catered to",
                "context": "Product accessibility"
            }
        ],
        "topic_tags": ["passives", "prepositional_verbs", "phrasal_verbs", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.c1.substitute-words-do-so-such-so-neither",
        "title": "Advanced Substitution and Verbal Pro-Forms: Do So, Such, and The Same",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Use formal pro-forms ('do so', 'do the same', 'such was...') to replace previously established verb phrases or entire propositions without clumsy repetition.",
        "summary_tr": "Önceki fiil öbeklerini veya hükümleri tekrarlamamak için 'do so', 'do the same' ve 'such' gibi gelişmiş ikame yapıları (substitution) kullanılır.",
        "explanation_en": [
            {
                "title": "Formal Verbal and Propositional Anaphora",
                "content": "'Do so' acts as a formal pro-form replacing an action verb phrase and its complements ('If you wish to export telemetry data, you may do so via the settings dashboard'). It requires dynamic, agentive verbs (cannot replace stative verbs like know or belong). 'Such' can function as a nominal or adjectival substitute referring back to a previously characterized state ('The server failed; such was the consequence of poor testing').",
                "patterns": [
                    "Subject + modal / auxiliary + do so (replacing dynamic verb phrase)",
                    "Such + be + Noun (propositional anaphora indicating intensity or nature)",
                    "do the same / do likewise"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'bunu yapmak / öyle yapmak' ifadesinin üst düzey resmi İngilizcedeki karşılığıdır. 'Eğer verileri indirmek isterseniz, bunu yapabilirsiniz' cümlesinde 'download' fiilini tekrarlamak yerine 'you may do so' denir. 'Do so' yalnızca iradeye bağlı eylem fiilleriyle kullanılır; 'understand' veya 'know' gibi durum fiilleriyle 'do so' denemez.",
        "rules": [
            {
                "name": "Dynamic Action Substitution Constraint",
                "pattern": "do so (dynamic actions only; stative verbs prohibited)",
                "use_cases": [
                    "Drafting formal API documentation, user manuals, and developer guidelines",
                    "Authoring legal terms of service and corporate governance bylaws"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Any customer wishing to dispute a charge must dispute the charge within thirty days.",
                "structure_b": "Any customer wishing to dispute a charge must do so within thirty days.",
                "difference_explanation_en": "Structure B replaces the awkward verbatim repetition of 'dispute a charge' with the elegant verbal pro-form 'do so'.",
                "difference_explanation_tr": "B yapısı hantal kelime tekrarı yerine zarif 'do so' ikame yapısını kullanarak üst düzey üslup oluşturur."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Durum Fiillerini 'Do So' ile İkame Etme",
                "explanation_tr": "'I loved the product and he did so too' yanlıştır çünkü 'love' durum fiilidir. Durum fiillerinde yalnızca yardımcı fiil tekrarlanır: 'and he did too'.",
                "incorrect_example": "The engineer understood the architecture, and I did so as well.",
                "correct_example": "The engineer understood the architecture, and I did as well."
            }
        ],
        "examples": [
            {
                "en": "Engineers who wish to bypass the standard canary rollout must do so only with VP approval.",
                "tr": "Standart kanarya dağıtımını atlamak isteyen mühendisler, bunu yalnızca genel müdür yardımcısı onayıyla yapabilirler.",
                "rule_highlight": "must do so (verbal pro-form replacing 'bypass standard rollout')",
                "context": "Deployment governance"
            },
            {
                "en": "If a team chooses to adopt an experimental framework, they must do so with full awareness of maintenance costs.",
                "tr": "Bir ekip deneysel bir çatı benimsemeyi seçerse, bunu bakım maliyetlerinin tam bilincinde olarak yapmalıdır.",
                "rule_highlight": "must do so",
                "context": "Technology radar"
            },
            {
                "en": "Such was the severity of the memory leak that the entire Kubernetes cluster became unresponsive.",
                "tr": "Bellek sızıntısının ciddiyeti öylesine büyüktü ki tüm Kubernetes kümesi yanıt vermez hale geldi.",
                "rule_highlight": "Such was the severity (inverted propositional substitute)",
                "context": "Incident retrospective"
            },
            {
                "en": "The mobile team upgraded their test suite, and the web squad did likewise.",
                "tr": "Mobil ekip test paketini güncelledi ve web takımı da benzer şekilde aynısını yaptı.",
                "rule_highlight": "did likewise (substitution)",
                "context": "Engineering alignment"
            }
        ],
        "topic_tags": ["substitution", "do_so", "pro_forms", "cohesion", "c1_grammar"],
        "status": "APPROVED",
        "version": 1
    }
]

print("C1 lessons count:", len(C1_LESSONS))
